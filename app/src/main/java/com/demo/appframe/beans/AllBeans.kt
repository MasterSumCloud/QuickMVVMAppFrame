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
}