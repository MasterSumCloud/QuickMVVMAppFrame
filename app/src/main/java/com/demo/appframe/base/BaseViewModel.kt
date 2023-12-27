package com.demo.appframe.base

import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.GsonUtils
import com.demo.appframe.core.Constent
import com.demo.appframe.ext.isNotEmptyOrNull
import com.demo.appframe.ext.logd
import com.demo.appframe.ext.request
import com.demo.appframe.util.GeneralUtil
import com.demo.appframe.util.Tos
import com.demo.appframe.net.apiService
import com.demo.appframe.util.SPUtil

open class BaseViewModel : ViewModel() {
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }
    var loginState = ObservableField<Boolean>(false)

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套
     */
    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { MutableLiveData<String>() }

        //隐藏
        val dismissDialog by lazy { MutableLiveData<Boolean>() }
    }

    fun login(type: Int?, token: String?) {
        val params = mutableMapOf<String, String?>()
        var finalType = ""
        when (type) {
            1 -> {//ali一键登录
                params.put("mb_token", token)
                finalType = "ali_auth"
            }
            2 -> {//微信登录
                params.put("we_code", token)
                finalType = "wx_auth"
            }
        }
        request({ apiService.goLogin(params) }, {
            GeneralUtil.loginSuccess(it, finalType)
            upgd(3, it?.user_id,null)
        }, { Tos.showToastShort(it.errorMsg) }, true)
    }

    /**
     * 广告追踪
     *  type	是	int	渠道号：1：oppo 2:baidu 3：vivo
    imei	是	string	二选一，但 优先用 imei，然后 考虑 ouId
    ou_id	是	string	二选一，但 优先用 imei，然后 考虑 ouId
    mac	否	string	mac地址
    data_type	是	int	推广类型：1激活 2支付 3注册 4次留
    order_no	否	string	如果类型为支付，需要提供订单号
    uid	否	string	安卓生成的uid ，data_type=4必传
    bp_uid	否	string	博派的uid ,data_type=3时，必传

     */
    fun upgd(dataType: Int, bpUid: String?, orderId: String?) {
        val pamras = HashMap<String, Any?>()
        val appChannelName = SPUtil.getAPPChannel()
        if (TextUtils.isEmpty(appChannelName)) {
            return
        }
        if (TextUtils.equals(appChannelName, Constent.CHANNEL_OPOO)) {
            pamras["type"] = 1
        } else if (appChannelName!!.startsWith("baidu")) {
            pamras["type"] = 2
        } else if (TextUtils.equals(appChannelName, Constent.CHANNEL_VIVO)) {
            pamras["type"] = 3
        } else if (TextUtils.equals(appChannelName, Constent.CHANNEL_XIAOMI)) {
            pamras["type"] = 4
        } else {
            pamras["type"] = 0
        }
//        val macAddress = DeviceUtils.getMacAddress()
//        val imei = getIMEI()
        val oaid = SPUtil.getOAID()
//        d("mac=" + macAddress + "imei=" + imei + "oaid=" + oaid)
//        pamras["imei"] = imei
        pamras["ou_id"] = oaid
        pamras["mac"] = ""
        pamras["data_type"] = dataType
        pamras["uid"] = GeneralUtil.getUUID()
        if (bpUid.isNotEmptyOrNull()) {
            pamras["bp_uid"] = bpUid
        }
        if (orderId.isNotEmptyOrNull()) {
            pamras["order_no"] = orderId
        }
        pamras["device_info"] = DeviceUtils.getModel()
        GsonUtils.toJson(pamras).logd("广澳最终")
        request({ apiService.appTuiGuangRegister(pamras) }, {
            SPUtil.putIsHasUpLoadAdZZ(true)
        }, {})
    }

}