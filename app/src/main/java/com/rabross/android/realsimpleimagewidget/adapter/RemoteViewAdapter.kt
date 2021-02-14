package com.rabross.android.realsimpleimagewidget.adapter

import android.content.Context

interface RemoteViewAdapter<VM, VH : RemoteViewsHolder> {
    fun onCreateRemoteViewsHolder(context: Context): VH
    fun onBindRemoteViewsHolder(viewModel: VM, remoteViewHolder: VH)
}