package com.rabross.android.realsimpleimagewidget.model

import android.widget.RemoteViews
import com.rabross.android.realsimpleimagewidget.R
import com.rabross.android.realsimpleimagewidget.imageloader.WidgetImageLoader

class WidgetPresenter(private val imageLoader: WidgetImageLoader) {

    fun render(viewModel: WidgetViewModel, remoteViews: RemoteViews) = with(viewModel) {
        imageLoader.load(R.id.widget, remoteViews, id, uri)
    }
}