package com.demo.appframe.core

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.view.View
import com.demo.appframe.R
import com.demo.appframe.act.LoginAct
import com.mobile.auth.gatewayauth.AuthRegisterViewConfig
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import com.nirvana.tools.core.AppUtils

class AliAuthLoginConfig(private val mContext: Activity, authHelper: PhoneNumberAuthHelper) {
    /**
     * 应用包名
     */
    private val mPackageName: String = AppUtils.getPackageName(mContext)
    private val mAuthHelper: PhoneNumberAuthHelper

    init {
        mAuthHelper = authHelper
        configAuthPage()
    }

    private fun configAuthPage() {
        mAuthHelper.removeAuthRegisterXmlConfig()
        mAuthHelper.removeAuthRegisterViewConfig()
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }
        mAuthHelper.addAuthRegistViewConfig("switch_msg",
            AuthRegisterViewConfig.Builder() //                .setView(initSwitchView(unit * 6))
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface { }
                .build())
        mAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.custom_port_action_bar, object : AbstractPnsViewDelegate() {
                    override fun onViewCreated(view: View) {
                        findViewById(R.id.btn_close).setOnClickListener { mAuthHelper.quitLoginPage() }
                        findViewById(R.id.view_click_wechat_login).setOnClickListener {
                            val intent = Intent(mContext, LoginAct::class.java)
                            intent.putExtra(Constent.LOGIN_ACT_TYPE, 2)
                            mContext.startActivity(intent)
                            mAuthHelper.quitLoginPage()
                        }
                        findViewById(R.id.view_click_phone_login).setOnClickListener {
                            val intent = Intent(mContext, LoginAct::class.java)
                            intent.putExtra(Constent.LOGIN_ACT_TYPE, 1)
                            mContext.startActivity(intent)
                            mAuthHelper.quitLoginPage()
                        }
                    }
                })
                .build()
        )
        var sCC = mAuthHelper.currentCarrierName
        when (sCC) {
            "CMCC" -> sCC = "移动"
            "CUCC" -> sCC = "联通"
            "CTCC" -> sCC = "电信"
        }
        //        boolean appPrivacyState = SPUtil.INSTANCE.getAPPPrivacyState();
        mAuthHelper.setAuthUIConfig(
            AuthUIConfig.Builder()
                .setAppPrivacyOne("《用户协议》", UCS.YHXY_URL)
                .setAppPrivacyTwo("《隐私政策》", UCS.YSZC_URL)
                .setAppPrivacyColor(Color.parseColor("#484c58"), Color.parseColor("#3F74F6"))
                .setPrivacyConectTexts(arrayOf(",", "和"))
                .setPrivacyOperatorIndex(2)
                .setPrivacyState(false)
                .setCheckboxHidden(false)
                .setUncheckedImgPath("icon_box_unchecked")
                .setCheckedImgPath("icon_box_checked")
                .setNavHidden(true)
                .setSwitchAccHidden(true)
                .setNavReturnHidden(true)
                .setDialogBottom(false)
                .setLogoHidden(false) //自定义协议页跳转协议，需要在清单文件配置自定义intent-filter，不需要自定义协议页，则请不要配置ProtocolAction
                //                .setProtocolAction("com.aliqin.mytel.protocolWeb")
                .setPackageName(mPackageName)
                .setNavColor(Color.TRANSPARENT)
                .setWebNavColor(Color.parseColor("#3F74F6"))
                .setWebNavTextColor(Color.WHITE)
                .setStatusBarColor(Color.parseColor("#17171F"))
                .setLogoOffsetY(62)
                .setLogoWidth(86)
                .setLogoHeight(86)
                .setLogoImgPath("ic_launcher")
                .setNumFieldOffsetY(188)
                .setNumberSizeDp(20) //设置字体大小，以Dp为单位，不同于Sp，不会随着系统字体变化而变化
                //                .setLogBtnWidth(dialogWidth - SizeUtils.dp2px(40))
                .setLogBtnMarginLeftAndRight(20)
                .setLogBtnHeight(44)
                .setLogBtnTextSizeDp(16)
                .setLogBtnBackgroundPath("shape_bg_blue_c22")
                .setLogBtnOffsetY(284)
                .setSloganText("中国" + sCC + "提供认证服务")
                .setSloganOffsetY(222)
                .setSloganTextSizeDp(14)
                .setPrivacyOffsetY(350) //                .setPageBackgroundPath("dialog_page_background")
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setScreenOrientation(authPageOrientation)
                .setNumberColor(Color.WHITE)
                .create()
        )
    }
}