package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.App
import com.demo.appframe.R
import com.demo.appframe.base.BaseViewModel
import com.demo.appframe.beans.MyActFunListItemBean
import com.demo.appframe.beans.MyfmHeaderData
import com.demo.appframe.core.Toggles
import com.demo.appframe.util.SPUtil

class FrmMyVM : BaseViewModel() {

    val userName = ObservableField("去登陆/注册")
    val userId = ObservableField("欢迎使用")
    val headerImageUrl = ObservableField("")
    val vipTime = ObservableField("成为会员，立享会员专属特权")
    val openVip = ObservableField("马上开通")
    val tablist = mutableListOf<MyActFunListItemBean>()
    val notifyHeader = ObservableField(0)

    fun setLoginInfo() {
        if (App.isLogin) {
            val info = App.myActInfo
            if (info != null) {
                userId.set(info.user_id)
                userName.set(info.nick_name)
                if (App.isVip) {
                    vipTime.set("有效期至 ${info.vip_limit_time}")
                    openVip.set("立即查看")
                }
                headerImageUrl.set(info.img)
            }
            notifyHeader.set(notifyHeader.get()?.plus(1))
        } else {
            headerImageUrl.set("")
            vipTime.set("成为会员，立享会员专属特权")
            userName.set("去登陆/注册")
            userId.set("欢迎使用")
            openVip.set("马上开通")
            if (tablist.isNotEmpty()) {
                tablist[0].myfmHeaderData = MyfmHeaderData(userName.get(), userId.get(), headerImageUrl.get(), vipTime.get(), openVip.get())
            }
            notifyHeader.set(notifyHeader.get()?.plus(1))
        }
    }


}