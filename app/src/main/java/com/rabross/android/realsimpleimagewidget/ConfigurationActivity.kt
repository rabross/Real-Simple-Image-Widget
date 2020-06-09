package com.rabross.android.realsimpleimagewidget

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

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

    override fun onStart() {
        super.onStart()
        if (SDK_INT < Build.VERSION_CODES.M || hasPermission()) startActivityForImageUri()
        else requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    startActivityForImageUri()
                    return
                }
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun hasPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun startActivityForImageUri() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Test"), REQUEST_CODE)
    }
}