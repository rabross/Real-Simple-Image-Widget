package com.rabross.android.realsimpleimagewidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ConfigurationActivity : AppCompatActivity() {

    private val REQUEST_CODE = 8962
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            appWidgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
        } ?: finish()
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
                Intent.createChooser(intent, null),
                REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.data?.let {

                        val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                        preferences.edit().putString(appWidgetId.toString(), it.toString()).apply()

                        val adapter = RemoteViewAdapter(this.applicationContext)

                        AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, adapter.createView())
                        adapter.bind(appWidgetId, it)

                        val resultValue = Intent()
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        setResult(Activity.RESULT_OK, resultValue)
                    }
                }
            }
        }
        finish()
    }
}