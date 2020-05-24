package com.rabross.android.realsimpleimagewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.AppWidgetTarget

class RemoteViewAdapter(private val context: Context) {

    private val remoteViews: RemoteViews = RemoteViews(context.packageName, R.layout.widget)

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
                .diskCacheStrategy(DiskCacheStrategy.DATA)
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
