package com.demo.appframe.dialog

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.demo.appframe.R
import com.demo.appframe.act.WebUrlAct
import com.demo.appframe.base.BaseDialog
import com.demo.appframe.core.Constent
import com.demo.appframe.core.UCS
import com.demo.appframe.util.GeneralUtil

class SplashDialog(context: Context) : BaseDialog(context) {
    private var keyListener: onKeyListener? = null
    private var okBtn: Button? = null
    private var cancle: TextView? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_splash
    }

    override fun initStyle() {
        window?.let {
            it.setGravity(Gravity.CENTER)
            it.attributes.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(56f)
            it.attributes.height = (GeneralUtil.getScreentPointOfApp() * 480).toInt()
        }
        setCancelable(false)
    }

    override fun initView() {
        okBtn = findViewById<Button>(R.id.btn_agree)
        cancle = findViewById<TextView>(R.id.tv_disagree)
        val tvAppInfo = findViewById<TextView>(R.id.tv_app_intro)
        val appName = context.getString(R.string.app_name)
        val start = 46 + appName.length
        val spannableString = SpannableString(
            "我们依据法律法规收集、使用个人信息。在使用“$appName”软件及相关服务前，请您务必仔细阅读并理解我们的《用户协议》及《隐私政策》。您一旦选择“同意”，即意味着您授权我们收集、保存使用、共享、披露及保护您的信息。"
        )

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(context, WebUrlAct::class.java)
                intent.putExtra(Constent.WEB_URL, UCS.YHXY_URL)
                intent.putExtra(Constent.TITLE_TEXT, "用户协议")
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(context, R.color.blue_3F74F6)
            }
        }, start, start + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent2 = Intent(context, WebUrlAct::class.java)
                intent2.putExtra(Constent.WEB_URL, UCS.YSZC_URL)
                intent2.putExtra(Constent.TITLE_TEXT, "隐私政策")
                context.startActivity(intent2)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(context, R.color.blue_3F74F6)
            }
        }, start + 7, start + 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvAppInfo.append(spannableString)
        tvAppInfo.setHintTextColor(ContextCompat.getColor(context, R.color.trans))
        tvAppInfo.movementMethod = LinkMovementMethod.getInstance()

        okBtn?.setOnClickListener(View.OnClickListener { keyListener?.onClickOk() })

        cancle?.setOnClickListener {
            keyListener?.onClickCancel()
            dismiss()
        }
    }

    interface onKeyListener {
        fun onClickOk()
        fun onClickCancel()
    }

    fun setOnKeyListener(keyListener: onKeyListener) {
        this.keyListener = keyListener
    }
}