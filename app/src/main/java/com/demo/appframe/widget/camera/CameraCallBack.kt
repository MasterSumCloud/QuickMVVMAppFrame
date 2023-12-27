package com.demo.appframe.widget.camera

import android.net.Uri

interface CameraCallBack {

    fun ratioCallBack(ratio: Int?)
    fun takePictureStatus(success: Boolean, msg: String, uri: Uri? = null)
}