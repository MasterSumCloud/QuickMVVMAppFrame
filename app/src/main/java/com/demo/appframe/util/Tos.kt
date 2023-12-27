package com.demo.appframe.util

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils

object Tos {
    fun showToastShort(msg: String?) {
        val toast = ToastUtils.make().setGravity(Gravity.CENTER, 0, 0)
        toast.setTextColor(Color.WHITE)
        toast.setBgColor(Color.parseColor("#B4000000"))
        toast.setDurationIsLong(false)
        toast.show(msg)
    }

    fun showToastLong(msg: String?) {
        val toast = ToastUtils.make().setGravity(Gravity.CENTER, 0, 0)
        toast.setTextColor(Color.WHITE)
        toast.setBgColor(Color.parseColor("#B4000000"))
        toast.setDurationIsLong(true)
        toast.show(msg)
    }
}