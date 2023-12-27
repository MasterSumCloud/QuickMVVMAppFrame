package com.demo.appframe.core

object UCS {
    var BASE_URL =
        if (Toggles.isRelease) "" else if (Toggles.isPreRelease) "" else ""

    const val TIME_CLICK_INTERVAL = 1500 //default click dis is 1500
    const val SIZE_OF_M = 1024 * 1024
    const val YSZC_URL =
        ""
    const val YHXY_URL =
        ""
    const val ZDXF_URL =
        ""
    const val REGULAR_OF_URL = "(http|https)://[A-Za-z0-9_\\-\\+.:?&@=/%#,;]*"

    const val WX_PAY_STR = "wx"
    const val ALI_PAY_STR = "ali"

}