package com.rabross.android.realsimpleimagewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.rabross.android.realsimpleimagewidget.adapter.WidgetRemotesViewAdapter
import com.rabross.android.realsimpleimagewidget.model.WidgetPresenter
import com.rabross.android.realsimpleimagewidget.model.WidgetViewModel
import com.rabross.android.realsimpleimagewidget.imageloader.GlideWidgetImageLoader

open class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        restoreActiveWidgets(context, appWidgetManager, appWidgetIds)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun restoreActiveWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val packageName = context.packageName
        val imageLoader = GlideWidgetImageLoader(context.applicationContext)
        val presenter = WidgetPresenter(imageLoader)
        val remoteViewAdapter = WidgetRemotesViewAdapter(presenter, packageName)

        for (appWidgetId in appWidgetIds) {
            preferences.getString(appWidgetId.toString(), null)?.let { uriString ->
                val viewHolder = remoteViewAdapter.onCreateRemoteViewsHolder(context)
                val viewModel = WidgetViewModel(appWidgetId, uriString)
                appWidgetManager.updateAppWidget(appWidgetId, viewHolder.remoteViews)
                remoteViewAdapter.onBindRemoteViewsHolder(viewModel, viewHolder)
            }
        }
    }
}

class Widget2x2 : WidgetProvider()
