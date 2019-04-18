package com.rabross.android.realsimpleimagewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

open class WidgetProvider : AppWidgetProvider() {

    private lateinit var preferences: SharedPreferences

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

        restoreActiveWidgets(context, appWidgetManager, appWidgetIds)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun restoreActiveWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        val adapter = RemoteViewAdapter(context.applicationContext)

        for (appWidgetId in appWidgetIds) {
            preferences.getString(appWidgetId.toString(), null)?.let {
                appWidgetManager.updateAppWidget(appWidgetId, adapter.createView())
                adapter.bind(appWidgetId, Uri.parse(it))
            }
        }
    }
}

class Widget2x2 : WidgetProvider()
