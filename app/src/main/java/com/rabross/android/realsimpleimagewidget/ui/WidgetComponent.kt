package com.rabross.android.realsimpleimagewidget.ui

import android.widget.RemoteViews
import com.rabross.android.realsimpleimagewidget.R
import com.rabross.android.realsimpleimagewidget.model.WidgetPresenter
import com.rabross.android.realsimpleimagewidget.model.WidgetViewModel

class WidgetComponent(private val presenter: WidgetPresenter, packageName: String): Component<WidgetViewModel> {

    internal val remoteViews = RemoteViews(packageName, R.layout.widget)

    override fun render(viewModel: WidgetViewModel) {
        presenter.render(viewModel, remoteViews)
    }
}