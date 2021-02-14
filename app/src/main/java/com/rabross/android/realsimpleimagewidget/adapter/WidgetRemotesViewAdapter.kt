package com.rabross.android.realsimpleimagewidget.adapter

import android.content.Context
import com.rabross.android.realsimpleimagewidget.model.WidgetPresenter
import com.rabross.android.realsimpleimagewidget.model.WidgetViewModel
import com.rabross.android.realsimpleimagewidget.ui.WidgetComponent

class WidgetRemotesViewAdapter(private val presenter: WidgetPresenter, private val packageName: String) : RemoteViewAdapter<WidgetViewModel, WidgetRemoteViewsHolder> {

    override fun onCreateRemoteViewsHolder(context: Context): WidgetRemoteViewsHolder {
        return WidgetRemoteViewsHolder(WidgetComponent(presenter, packageName))
    }

    override fun onBindRemoteViewsHolder(viewModel: WidgetViewModel, remoteViewHolder: WidgetRemoteViewsHolder) {
       remoteViewHolder.widgetComponent.render(viewModel)
    }
}

class WidgetRemoteViewsHolder(val widgetComponent: WidgetComponent) : RemoteViewsHolder {
    override val remoteViews = widgetComponent.remoteViews
}