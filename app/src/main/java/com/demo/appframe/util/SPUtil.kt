package com.demo.appframe.util

import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.demo.appframe.App
import com.demo.appframe.beans.LoginResponBean
import com.demo.appframe.beans.SupportBeanItem
import com.demo.appframe.core.Constent
import com.demo.appframe.core.Toggles
import com.demo.appframe.ext.isNotEmptyOrNull
import com.google.gson.reflect.TypeToken

object SPUtil {

    //保存登录的一些信息
    fun putLoginInfoBean(loginInfo: String?) {
        if (loginInfo.isNotEmptyOrNull()) {
            SPUtils.getInstance().put(Constent.LOGIN_BEAN, loginInfo)
        }
    }

    //获取登录保存的bean
    fun getLoginInfoBean(): LoginResponBean? {
        val json = SPUtils.getInstance().getString(Constent.LOGIN_BEAN)
        if (json.isNotEmptyOrNull()) {
            val loginInfoBean =
                GsonUtils.fromJson<LoginResponBean>(json, LoginResponBean::class.java)
            return loginInfoBean
        } else {
            return null
        }
    }


    //保存token
    fun putToken(token: String?) {
        App.token = token
        SPUtils.getInstance().put(Constent.APP_TOKEN, token)
    }

    //获取token
    fun getToken(): String? {
        if (TextUtils.isEmpty(App.token)) {
            App.token = SPUtils.getInstance().getString(Constent.APP_TOKEN)
        }
        return App.token
    }

    //判断登录状态
    fun getAppIsLogin(): Boolean {
        return !TextUtils.isEmpty(getToken())
    }

    fun putMessagePushState(state: Boolean) {
        SPUtils.getInstance().put(Constent.SWITCH_STATE_OF_MESSAGE_PUSH, state)
    }

    fun getMessagePushState(): Boolean {
        return SPUtils.getInstance().getBoolean(Constent.SWITCH_STATE_OF_MESSAGE_PUSH)
    }

    fun putAPPChannel(channelName: String?) {
        SPUtils.getInstance().put(Constent.APP_CHANNEL, channelName)
    }

    fun getAPPChannel(): String? {
        if (Toggles.isShowTesting) {
            return "test"
        }
        return SPUtils.getInstance().getString(Constent.APP_CHANNEL)
    }

    //是否是第一次登录
    fun getIsFirstStartApp(): Boolean {
        return SPUtils.getInstance().getBoolean(Constent.FIRST_START_APP, true)
    }

    fun putIsFirstStartApp(isFirst: Boolean) {
        SPUtils.getInstance().put(Constent.FIRST_START_APP, isFirst)
    }


    fun putSecondOpenApp(second: Boolean) {
        SPUtils.getInstance().put(Constent.APP_SECOND_STARTED, second)
    }

    fun getSecondOpenApp(): Boolean {
        return SPUtils.getInstance().getBoolean(Constent.APP_SECOND_STARTED, false)
    }

    fun putUUID(uid: String) {
        SPUtils.getInstance().put(Constent.UUID, uid)
    }

    fun putOAID(oaid: String?) {
        SPUtils.getInstance().put(Constent.OAID, oaid)
    }

    fun getOAID(): String? {
        return SPUtils.getInstance().getString(Constent.OAID)
    }

    /**
     * 广告追踪 仅上1次
     */
    fun putIsHasUpLoadAdZZ(hasuplod: Boolean) {
        SPUtils.getInstance().put(Constent.UPLOAD_AD_ZZ, hasuplod)
    }

    fun getHasUploadADZZ(): Boolean {
        return SPUtils.getInstance().getBoolean(Constent.UPLOAD_AD_ZZ, false)
    }

    fun putVip(vip: Boolean) {
        SPUtils.getInstance().put(Constent.VIP_STATE, vip)
    }

    fun getVip(): Boolean {
        return SPUtils.getInstance().getBoolean(Constent.VIP_STATE, false)
    }

    fun putLoginType(type: Int) {
        SPUtils.getInstance().put(Constent.LOGIN_TYPE_DEFAULT, type)
    }

    fun getLoginType(): Int {
        return SPUtils.getInstance().getInt(Constent.LOGIN_TYPE_DEFAULT, 1)
    }


}