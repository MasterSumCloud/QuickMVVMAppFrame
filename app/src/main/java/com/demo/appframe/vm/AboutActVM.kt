package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.App
import com.demo.appframe.base.BaseViewModel
import com.nirvana.tools.core.AppUtils

class AboutActVM : BaseViewModel() {
    var appversion = ObservableField<String>()

    fun versionTx() {
        appversion.set("版本号：" + AppUtils.getVersionName(App.app))
    }
}