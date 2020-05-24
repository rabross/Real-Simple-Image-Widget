package com.rabross.android.realsimpleimagewidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val REQUEST_CODE = 8962

class ConfigurationActivity : AppCompatActivity() {

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

        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
                Intent.createChooser(intent, null),
                REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        fun cacheUri(uri: String) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE)
                    .edit()
                    .putString(appWidgetId.toString(), uri)
                    .apply()
        }

        fun bind(uri: String) {
            val adapter = RemoteViewAdapter(this.applicationContext)
            AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, adapter.getView())
            adapter.bind(appWidgetId, uri)
        }

        fun setResultValue() {
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
        }

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.data?.let { uri ->
                        uri.toString().also { uriString ->
                            cacheUri(uriString)
                            bind(uriString)
                        }
                        setResultValue()
                    }
                }
            }
        }

        finish()
    }
}