package com.demo.appframe.beans

data class SupportBeanItem(
    val id: String,
    val image: String,
    val name: String
)

data class MyActFunListItemBean(
    var icon: Int, var text: String, var showArr: Boolean, var showCheckBox: Boolean
) {
    var switchChecked = false
    var myfmHeaderData: MyfmHeaderData? = null
}

data class MyfmHeaderData(
    val userName: String?,
    val userId: String?,
    val headerImageUrl: String?,
    val vipTime: String?,
    val openVip: String?
)