package com.demo.appframe.act

import android.content.Intent
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.RegexUtils
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.core.AppConfig.WECHAT_APPID
import com.demo.appframe.core.Constent
import com.demo.appframe.core.UCS
import com.demo.appframe.databinding.ActLoginBinding
import com.demo.appframe.dialog.LoginYSZCDialog
import com.demo.appframe.ext.observe
import com.demo.appframe.util.Tos.showToastShort
import com.demo.appframe.vm.LoginActVM
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class LoginAct : BaseVMActivity<LoginActVM, ActLoginBinding>() {
    private var mCurrentShowState: Int = 2
    private lateinit var mLoginYSZCDialog: LoginYSZCDialog
    private var mCountDownTimer: CountDownTimer? = object : CountDownTimer(60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            selfVB.tvGetcode.run {
                text = (millisUntilFinished / 1000).toString() + "s"
                setTextColor(getColor(R.color.gray_C6C5CC))
                isEnabled = false
            }
        }

        override fun onFinish() {
            selfVB.tvGetcode.run {
                text = "获取验证码"
                isEnabled = true
                setTextColor(getColor(R.color.black_23234c))
            }
            selfVM.codeState.set(null)
        }
    }

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        laterUnRegisetEventBus = true
        setStateBarColor(false, R.color.black_17171f)
        mLoginYSZCDialog = LoginYSZCDialog(this)
        mCurrentShowState = intent.getIntExtra(Constent.LOGIN_ACT_TYPE, mCurrentShowState)
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_login
    }

    override fun initSelfViews() {
        setAllowTextSp()
        changeLayout(mCurrentShowState)
    }

    private fun setAllowTextSp() {
        val spannableString = SpannableString("已阅读并同意《用户协议》和《隐私政策》")
        selfVB.textView31.movementMethod = LinkMovementMethod.getInstance()
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(
                    Intent(
                        this@LoginAct, WebUrlAct::class.java
                    ).putExtra(Constent.TITLE_TEXT, "用户协议")
                        .putExtra(Constent.WEB_URL, UCS.YHXY_URL)
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = getColor(R.color.blue_3F74F6)
                ds.isUnderlineText = false
            }
        }, 6, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(
                    Intent(
                        this@LoginAct, WebUrlAct::class.java
                    ).putExtra(Constent.TITLE_TEXT, "隐私政策")
                        .putExtra(Constent.WEB_URL, UCS.YSZC_URL)
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = getColor(R.color.blue_3F74F6)
                ds.isUnderlineText = false
            }
        }, 13, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        selfVB.textView31.text = spannableString
    }

    override fun initSelfListener() {

        selfVM.codeState.observe {
            if ("getCode".equals(it)) {
                selfVM.getPhoneCode()
                mCountDownTimer?.start()
            }
        }

        selfVM.loginState.observe {
            if (it) {
                this@LoginAct.finish()
            }
        }

        mLoginYSZCDialog.setOnKeyListener(object : LoginYSZCDialog.onYSListener {
            override fun onAgree() {
                selfVM.privateChecked.set(true)
                selfVB.viewLoginClick.callOnClick()
            }

            override fun onDisAgree() {

            }

        })
    }

    override fun singeClick(v: View?) {
        when (v?.id) {
            R.id.tv_getcode -> {
                getphoneCode()
            }

            R.id.view_wechat_login_click -> {
                if (mCurrentShowState == 2) {
                    selfVM.startAuthLogin()
                } else {
                    changeLayout(2)
                }
            }

            R.id.view_phone_login_click -> {
                if (mCurrentShowState == 2) {
                    changeLayout(1)
                } else {
                    selfVM.startAuthLogin()
                }
            }
        }
    }


    private fun startLogin() {
        if (mCurrentShowState == 2) {
            if (!selfVM.privateChecked.get()!!) {
                mLoginYSZCDialog.show()
            } else {
                val wxapi = WXAPIFactory.createWXAPI(this, WECHAT_APPID)
                if (wxapi.isWXAppInstalled) {
                    val req = SendAuth.Req()
                    req.scope = "snsapi_userinfo"
                    req.state = "wechat_sdk_demo_test"
                    wxapi.sendReq(req)
                } else {
                    showToastShort(getString(R.string.wechat_not_install))
                }
            }
        } else {
            val phone = selfVM.phoneNumber.get()
            val code = selfVM.code.get()
            if (TextUtils.isEmpty(phone)) {
                showToastShort("请输入手机号")
            } else if (TextUtils.isEmpty(code)) {
                showToastShort("请输入验证码")
            } else if (!RegexUtils.isMobileSimple(phone.toString().trim())) {
                showToastShort("请输入正确的手机号")
            } else if (code.toString().trim().length != 6) {
                showToastShort("请输入正确的验证码")
            } else if (!selfVM.privateChecked.get()!!) {
                mLoginYSZCDialog.show()
            } else {
                selfVM.goLogin()
            }
        }

    }

    private fun getphoneCode() {
        if (TextUtils.isEmpty(selfVM.phoneNumber.get())) {
            showToastShort("请输入手机号")
        } else if (!RegexUtils.isMobileSimple(selfVM.phoneNumber.get()?.trim())) {
            showToastShort("请输入正确的手机号")
        } else {
            selfVM.codeState.set("getCode")
        }
    }


    override fun normalClick(v: View?) {
        when (v?.id) {
            R.id.view_login_click -> {
                startLogin()
            }
        }
    }

    override fun bindVBM(viewBinding: ActLoginBinding, viewMode: LoginActVM) {
        viewBinding.vm = viewMode
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    private fun changeLayout(show1or2: Int) {
        mCurrentShowState = show1or2
        //创建2个约束的集合
        val constraintSet1 = ConstraintSet()
        val constraintSet2 = ConstraintSet()
        //复制各自布局的约束
        constraintSet1.clone(this, R.layout.act_login)
        constraintSet2.clone(this, R.layout.act_login2)
        if (show1or2 == 1) {
            TransitionManager.beginDelayedTransition(selfVB.clMainView)
            constraintSet1.applyTo(selfVB.clMainView)
            selfVB.tvLeftBtmText.text = "微信登录"
            selfVB.tvRightBtmText.text = "本机号码一键登录"
            selfVB.ivLeftBtnIcon.setBackgroundResource(R.mipmap.icon_wechat_round)
            selfVB.btnLogin.text = "登录"
        } else {
            TransitionManager.beginDelayedTransition(selfVB.clMainView);
            constraintSet2.applyTo(selfVB.clMainView)
            selfVB.tvLeftBtmText.text = "本机号码一键登录"
            selfVB.tvRightBtmText.text = "验证码登录"
            selfVB.ivLeftBtnIcon.setBackgroundResource(R.mipmap.icon_phome_autologin)
            selfVB.btnLogin.text = "微信一键登录"
        }
    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        finish()
    }

    override fun onResume() {
        super.onResume()
        selfVM.loadingChange.dismissDialog.value = false
    }

}