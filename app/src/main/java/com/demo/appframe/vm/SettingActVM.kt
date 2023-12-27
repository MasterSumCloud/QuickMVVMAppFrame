package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.demo.appframe.event.LoginMessageEvent
import com.demo.appframe.App
import com.demo.appframe.base.BaseViewModel
import com.demo.appframe.beans.SetBean
import com.demo.appframe.core.Constent
import com.demo.appframe.ext.request
import com.demo.appframe.net.apiService
import com.demo.appframe.util.GeneralUtil
import com.demo.appframe.util.SPUtil
import com.demo.appframe.util.Tos
import org.greenrobot.eventbus.EventBus

class SettingActVM : BaseViewModel() {

    val itemFuncList = ObservableField<MutableList<SetBean>>(getFunsList())
    val outLoginOrUnregist = ObservableField<Boolean>()
    val isAppLogin = ObservableField(App.isLogin)

    fun getFunsList(): MutableList<SetBean> {
        val setbeanlist = mutableListOf<SetBean>()
        val cacheSize = CacheDiskStaticUtils.getCacheSize()
//        val fileLibSize = File(GeneralUtil.getCacheOfFilelibtxtPath()).length()
        setbeanlist.add(SetBean(1, "清理缓存", true, GeneralUtil.getFileSize(cacheSize), false))
//        setbeanlist.add(SetBean(2, "检查更新", true, "", false))
        setbeanlist.add(SetBean(2, "关于我们", true, "", false))
        if (App.isLogin) {
            setbeanlist.add(SetBean(2, "注销账号", false, "", false))
        }
        return setbeanlist
    }

    fun unRegistUser() {
        request({ apiService.unRegist() }, {
            outUser()
        }, {
            outUser()
        }, true)
    }

    private fun outUser() {
        SPUtil.putLoginInfoBean(null)
        SPUtil.putToken(null)
        App.isLogin = false
        App.isVip = false
        App.token = null
        App.myActInfo = null
        outLoginOrUnregist.set(true)
        EventBus.getDefault().post(LoginMessageEvent(Constent.LOGIN_OUT, null, 1))
        Tos.showToastShort("退出成功")
    }
}