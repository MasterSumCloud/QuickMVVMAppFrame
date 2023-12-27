package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.App
import com.demo.appframe.R
import com.demo.appframe.base.BaseViewModel
import com.demo.appframe.beans.MyActFunListItemBean
import com.demo.appframe.core.Toggles
import com.demo.appframe.util.SPUtil

class FrmMyVM : BaseViewModel() {

    val userName = ObservableField("去登陆/注册")
    val userId = ObservableField("欢迎使用")
    val headerImageUrl = ObservableField("")
    val vipTime = ObservableField("成为会员，立享会员专属特权")
    val openVip = ObservableField("马上开通")
    val bindText = ObservableField("账号绑定")


    fun createFuncListData(): MutableList<MyActFunListItemBean> {
        val tablist = mutableListOf<MyActFunListItemBean>()
        tablist.add(MyActFunListItemBean(R.mipmap.myicon_service, "联系客服", true, false))
        tablist.add(MyActFunListItemBean(R.mipmap.myicon_yhxy, "用户协议", true, false))
        tablist.add(MyActFunListItemBean(R.mipmap.myicon_yszc, "隐私协议", true, false))
        val getj = MyActFunListItemBean(R.mipmap.myicon_notify, "个性化推荐", false, true)
        getj.switchChecked = SPUtil.getMessagePushState()
        tablist.add(getj)
        tablist.add(MyActFunListItemBean(R.mipmap.myicon_setting, "设置", true, false))
        if (Toggles.isShowTesting) {
            tablist.add(MyActFunListItemBean(R.mipmap.myicon_setting, "测试", true, false))
        }
        return tablist
    }

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
        } else {
            headerImageUrl.set("")
            vipTime.set("成为会员，立享会员专属特权")
            userName.set("去登陆/注册")
            userId.set("欢迎使用")
            openVip.set("马上开通")
        }
    }



}