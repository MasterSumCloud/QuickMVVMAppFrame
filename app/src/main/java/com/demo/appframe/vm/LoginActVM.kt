package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.App
import com.demo.appframe.base.BaseViewModel
import com.demo.appframe.ext.request
import com.demo.appframe.net.apiService
import com.demo.appframe.util.GeneralUtil
import com.demo.appframe.util.Tos

class LoginActVM : BaseViewModel() {
    var phoneNumber = ObservableField<String>()
    var code = ObservableField<String>()
    var codeState = ObservableField<String>()
    var privateChecked = ObservableField<Boolean>(false)
    var weChatCode = ObservableField<String>()
    val bindState = ObservableField(false)

    fun getPhoneCode() {
        request({ apiService.getPhoneCode(phoneNumber.get()!!) },
            {},
            { Tos.showToastShort(it.errorMsg) })
    }

    fun goLogin() {
        val params = mutableMapOf<String, String?>()
        params.put("phone", phoneNumber.get())
        params.put("code", code.get())
        request({ apiService.goLogin(params) }, {
            GeneralUtil.loginSuccess(it, "phone_auth")
            loginState.set(App.isLogin)
            upgd(3, it?.user_id, null)
        }, { Tos.showToastShort(it.errorMsg) })
    }

    fun startAuthLogin() {
        loadingChange.showDialog.value = "show"
        App.app.goLogin()
    }

}