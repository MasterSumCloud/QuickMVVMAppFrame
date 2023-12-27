package com.demo.appframe.beans

data class BuyResponseBackBean(
    val noncestr: String,
    val prepayid: String,
    val sign: String,
    val timestamp: Int,
    val order_no: String,
    val alipay: String
)

data class MediaFile(
    val fileName: String,
    val path: String,
    val fileSize: Long,
    val addTime: Long,
    val duration: Long,
    val firstImagePath: String,
    val mediaType: Int,//0图 1视频
    var select: Boolean
)