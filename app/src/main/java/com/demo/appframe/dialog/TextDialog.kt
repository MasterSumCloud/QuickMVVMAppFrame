package com.demo.appframe.dialog

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.demo.appframe.R
import com.demo.appframe.base.BaseDialog

class TextDialog(context: Context) : BaseDialog(context) {
    private var showContent: CharSequence? = null
    private var okBtnText: String? = "确定"
    private var cancelBtnText: String? = "取消"
    private var titleText: String? = "提示"
    private var keyListener: onTextDialogBtnListener? = null
    private var tvContent: TextView? = null
    private var tvTitle: TextView? = null
    private var okBtn: TextView? = null
    private var cancle: TextView? = null
    private var viewBg: View? = null


    override fun getLayoutId(): Int {
        return R.layout.dialog_text
    }

    override fun initStyle() {
        window?.let {
            it.setGravity(Gravity.CENTER)
            it.attributes.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)
        }
    }

    override fun initView() {
        okBtn = findViewById<TextView>(R.id.tv_confirm)
        cancle = findViewById<TextView>(R.id.tv_cancel)
        tvTitle = findViewById<TextView>(R.id.tv_title)
        tvContent = findViewById<TextView>(R.id.tv_content)
        viewBg = findViewById<View>(R.id.view_bg)

        tvContent?.text = showContent
        if (TextUtils.isEmpty(okBtnText)) {
            okBtn?.visibility = View.GONE
        }
        if (TextUtils.isEmpty(cancelBtnText)) {
            cancle?.visibility = View.GONE
        }

        okBtn?.text = okBtnText
        cancle?.text = cancelBtnText
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle?.text = titleText
        }

        okBtn?.setOnClickListener {
            keyListener?.onClickOk()
            dismiss()
        }


        cancle?.setOnClickListener {
            keyListener?.onClickCancel()
            dismiss()
        }
    }


    interface onTextDialogBtnListener {
        fun onClickOk()
        fun onClickCancel()
    }

    fun setOnKeyListener(keyListener: onTextDialogBtnListener) {
        this.keyListener = keyListener
    }

    fun showTextDialog(content: CharSequence, onKeyListener: onTextDialogBtnListener) {
        this.showContent = content
        this.keyListener = onKeyListener
        show()
    }

    fun showTextDialog(
        content: CharSequence,
        okBtnText: String?,
        cancelBtnText: String?,
        title: String?,
        onKeyListener: onTextDialogBtnListener?
    ) {
        this.showContent = content
        this.keyListener = onKeyListener
        this.okBtnText = okBtnText
        this.cancelBtnText = cancelBtnText
        this.titleText = title
        show()
    }
}