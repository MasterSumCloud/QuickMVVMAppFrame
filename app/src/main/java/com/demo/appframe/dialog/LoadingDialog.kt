package com.demo.appframe.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.demo.appframe.R
import com.demo.appframe.base.BaseDialog

class LoadingDialog (context: Context) : BaseDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun initStyle() {
        window?.let {
            it.setGravity(Gravity.CENTER)
            it.setDimAmount(0f)
        }
        setCancelable(true)
    }

    override fun initView() {

    }
}