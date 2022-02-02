package com.rabross.android.realsimpleimagewidget

import android.graphics.Bitmap

fun Bitmap.Config.bytesPerPixel(): Int {
    return when (this) {
        Bitmap.Config.ARGB_8888 -> 4
        else -> 1
    }
}