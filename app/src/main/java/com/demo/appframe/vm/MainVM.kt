package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.base.BaseViewModel

class MainVM : BaseViewModel() {
    val notifyUserInfo = ObservableField(0)
    val needGoVipAct = ObservableField(false)
    fun getBasicUserInfo() {

    }
}