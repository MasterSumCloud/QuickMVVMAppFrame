package com.demo.appframe.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.demo.appframe.R

abstract class BaseDialog(context: Context) : Dialog(context, R.style.DialogTipTheme),
    DefaultLifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Dialog>.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initStyle()
    }

    abstract fun getLayoutId(): Int

    abstract fun initStyle()

    abstract fun initView()

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dismiss()
    }
}