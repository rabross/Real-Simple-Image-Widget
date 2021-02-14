package com.rabross.android.realsimpleimagewidget.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.annotation.IdRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.target.AppWidgetTarget
import com.rabross.android.realsimpleimagewidget.R
import com.rabross.android.realsimpleimagewidget.util.bytesPerPixel
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.math.roundToInt
import kotlin.math.sqrt

class WidgetImageLoaderImpl(private val context: Context): WidgetImageLoader {

    override fun load(@IdRes viewIdRes: Int, remoteViews: RemoteViews, widgetId: Int, uri: String) {

        val displayMetrics = displayMetrics(context)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val screenSizeMultiplier = 1.5f

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .placeholder(R.drawable.ic_launcher)
                .fitCenter()
                .transform(SizeLimitingBitmapTransformation(width, height, screenSizeMultiplier))
                .into(AppWidgetTarget(context, viewIdRes, remoteViews, widgetId))
    }

    private fun displayMetrics(context: Context): DisplayMetrics {
        val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return DisplayMetrics().apply {
            windowManager.defaultDisplay.getMetrics(this)
        }
    }
}


private class SizeLimitingBitmapTransformation(val width: Int, val height: Int, val screenSizeMultiplier: Float) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bytesPerPixel = toTransform.config.bytesPerPixel()
        val sideScaleFactor = sqrt(byteLimit(bytesPerPixel) / bytesPerPixel / (outWidth * outHeight).toDouble())
        val scaledWidth = outWidth * sideScaleFactor
        val scaledHeight = outHeight * sideScaleFactor

        return Bitmap.createScaledBitmap(toTransform, scaledWidth.roundToInt(), scaledHeight.roundToInt(), false)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(javaClass.name.toByteArray(Charset.forName("UTF-8")))
    }

    private fun byteLimit(bytesPerPixel: Int) = width * height * bytesPerPixel * screenSizeMultiplier
}