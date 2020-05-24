package com.rabross.android.realsimpleimagewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

open class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        restoreActiveWidgets(context, appWidgetManager, appWidgetIds)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun restoreActiveWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

        for (appWidgetId in appWidgetIds) {
            val adapter = RemoteViewAdapter(context.applicationContext)
            preferences.getString(appWidgetId.toString(), null)?.let { uriString ->
                appWidgetManager.updateAppWidget(appWidgetId, adapter.getView())
                adapter.bind(appWidgetId, uriString)
            }
        }
    }
}

class Widget2x2 : WidgetProvider()
