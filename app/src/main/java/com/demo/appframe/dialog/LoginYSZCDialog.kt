package com.demo.appframe.dialog

import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import com.demo.appframe.R
import com.demo.appframe.base.BaseDialog

class LoginYSZCDialog(context: Context) : BaseDialog(context) {
    private var btnDisagree: TextView? = null
    private var btnAgree: TextView? = null
    private var keyListener: onYSListener? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_login_tip
    }

    override fun initStyle() {
        window?.let {
            it.setGravity(Gravity.CENTER)
        }
        setCancelable(true)
    }

    override fun initView() {
        btnAgree = findViewById<Button>(R.id.btn_diagree)
        btnDisagree = findViewById<Button>(R.id.btn_disagree)

        btnAgree?.setOnClickListener {
            keyListener?.onAgree()
            dismiss()
        }

        btnDisagree?.setOnClickListener {
            keyListener?.onDisAgree()
            dismiss()
        }
    }

    interface onYSListener {
        fun onAgree()
        fun onDisAgree()
    }

    fun setOnKeyListener(keyListener: onYSListener) {
        this.keyListener = keyListener
    }
}