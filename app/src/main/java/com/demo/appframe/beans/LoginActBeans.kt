package com.demo.appframe.beans

data class LoginResponseCodeBean(
    val dd: String
)

data class LoginResponBean(
    val img: String,
    val nick_name: String,
    val token: String?,
    val user_id: String,
    val vip: Boolean,
    val vip_limit_time: String?
)

data class UserInfoBean(
    val avatar_url: String,
    val created_at: String,
    val is_vip: Boolean,
    val mobile: String,
    val nick_name: String,
    val user_id: String,
    val vip_end_time: String,
    val surplus: MutableList<SurPlus>,

    ) {
    data class SurPlus(
        val type: Int,
        val surplus_time: Int
    )

}