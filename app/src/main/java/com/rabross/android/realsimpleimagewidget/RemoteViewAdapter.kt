package com.rabross.android.realsimpleimagewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.target.Target

class RemoteViewAdapter(val context: Context) {

    private lateinit var remoteViews: RemoteViews

    fun createView(): RemoteViews {
        remoteViews = RemoteViews(context.packageName, R.layout.widget)
        return remoteViews
    }

    fun bind(appWidgetId: Int, uri: Uri) {
        bindPendingIntent(appWidgetId)
        bindUri(appWidgetId, uri)
    }

    private fun bindUri(appWidgetId: Int, uri: Uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .fitCenter()
                .listener(object : RequestListener<Uri, Bitmap> {
                    override fun onException(e: Exception, model: Uri, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        remoteViews.setTextViewText(R.id.widget_text, context.getString(R.string.error_failed))
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Uri, target: Target<Bitmap>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        remoteViews.setViewVisibility(R.id.widget_text, View.GONE)
                        return false
                    }
                })
                .into(AppWidgetTarget(context, remoteViews, R.id.widget_image, appWidgetId))
    }

    private fun bindPendingIntent(appWidgetId: Int) {
        val configIntent = Intent(context, ConfigurationActivity::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_ONE_SHOT)

        remoteViews.setOnClickPendingIntent(R.id.widget_image, configPendingIntent)
    }
}
