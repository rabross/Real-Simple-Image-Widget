package com.rabross.android.realsimpleimagewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.target.AppWidgetTarget
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val bytesPerPixel = 4

class RemoteViewAdapter(private val context: Context) {

    private val remoteViews: RemoteViews = RemoteViews(context.packageName, R.layout.widget)

    private val maxBitmapSize: Long by lazy {

        val screenSizeMultiplier = 1.5

        val displayMetrics = displayMetrics(context)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        // Max bitmap memory usage
        // https://developer.android.com/reference/android/appwidget/AppWidgetManager#updateAppWidget(int[],%20android.widget.RemoteViews)
        (width * height * bytesPerPixel * screenSizeMultiplier).toLong()
    }

    fun getView(): RemoteViews {
        return remoteViews
    }

    fun bind(appWidgetId: Int, uri: String) {
        bindPendingIntent(appWidgetId)
        bindUri(appWidgetId, uri)
    }

    private fun bindUri(appWidgetId: Int, uri: String) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .placeholder(R.drawable.ic_launcher)
                .fitCenter()
                .transform(SizeLimitingBitmapTransformation(maxBitmapSize))
                .into(AppWidgetTarget(context, R.id.widget, remoteViews, appWidgetId))
    }

    private fun bindPendingIntent(appWidgetId: Int) {
        val configIntent = Intent(context, ConfigurationActivity::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.widget, configPendingIntent)
    }
}

class SizeLimitingBitmapTransformation(private val byteLimit: Long) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val sideScaleFactor = sqrt(byteLimit / bytesPerPixel / (outWidth * outHeight).toDouble())
        val scaledWidth = outWidth * sideScaleFactor
        val scaledHeight = outHeight * sideScaleFactor

        return Bitmap.createScaledBitmap(toTransform, scaledWidth.roundToInt(), scaledHeight.roundToInt(), false)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(javaClass.name.toByteArray(Charset.forName("UTF-8")))
    }
}

fun displayMetrics(context: Context): DisplayMetrics {
    val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }
}
