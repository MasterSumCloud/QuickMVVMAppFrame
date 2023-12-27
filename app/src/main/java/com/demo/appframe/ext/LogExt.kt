package com.demo.appframe.ext

import com.demo.appframe.util.LogU


const val TAG = "Log-App-Project"

fun String.logd(tag: String = TAG) = LogU.d(tag, this)
fun String.loge(tag: String = TAG) = LogU.d(tag, this)
fun String.logHttp(tag: String = TAG) = LogU.d(tag, this)
