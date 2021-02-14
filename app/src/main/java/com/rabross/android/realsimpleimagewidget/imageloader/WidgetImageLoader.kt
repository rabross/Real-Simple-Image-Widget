package com.rabross.android.realsimpleimagewidget.imageloader

import android.widget.RemoteViews
import androidx.annotation.IdRes

interface WidgetImageLoader {
    fun load(@IdRes viewIdRes: Int, remoteViews: RemoteViews, widgetId: Int, uri: String)
}