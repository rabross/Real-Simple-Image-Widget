package com.rabross.android.realsimpleimagewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.target.Target

class RemoteViewAdapter(val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private lateinit var remoteViews: RemoteViews
    private var width: Int = 0
    private var height: Int = 0

    init {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        width = size.x
        height = size.y
    }

    fun createView(): RemoteViews {
        remoteViews = RemoteViews(context.packageName, R.layout.widget)
        return remoteViews
    }

    fun bind(appWidgetId: Int, uri: Uri) {
        bindPendingIntent(appWidgetId)
        bindUri(appWidgetId, uri)
    }

    private fun bindUri(appWidgetId: Int, uri: Uri) {

        Glide.with(context.applicationContext)
                .asBitmap()
                .override(width, height)
                .load(uri)
                .fitCenter()
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        remoteViews.setViewVisibility(R.id.widget_text, View.GONE)
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        remoteViews.setTextViewText(R.id.widget_text, context.getString(R.string.error_failed))
                        return false
                    }
                })
                .into(AppWidgetTarget(context, R.id.widget_image, remoteViews, appWidgetId))
    }

    private fun bindPendingIntent(appWidgetId: Int) {
        val configIntent = Intent(context, ConfigurationActivity::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_ONE_SHOT)

        remoteViews.setOnClickPendingIntent(R.id.widget_image, configPendingIntent)
    }
}
