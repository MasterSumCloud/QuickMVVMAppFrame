package com.demo.appframe.act

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.demo.appframe.App
import com.demo.appframe.MainActivity
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.databinding.ActivitySplashBinding
import com.demo.appframe.dialog.SplashDialog
import com.demo.appframe.util.SPUtil
import com.demo.appframe.vm.SplashActVM

class SplashAct : BaseVMActivity<SplashActVM, ActivitySplashBinding>() {
    private lateinit var handler: Handler
    var splashDialog: SplashDialog? = null

    override fun onResume() {
        super.onResume()
        if (SPUtil.getIsFirstStartApp()) {
            splashDialog?.show()
        } else {
            handler.sendEmptyMessageDelayed(1, 1500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (splashDialog != null) {
            splashDialog?.dismiss()
            splashDialog = null
        }
    }

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        hideTitleBarAndStateBar(false)

        BarUtils.setNavBarVisibility(this, false)
//        setStateBarColor(true, R.color.black_362F57)
        splashDialog = SplashDialog(this)
        handler = Handler(Looper.getMainLooper(), Handler.Callback {
            startActivity(Intent(this@SplashAct, MainActivity::class.java))
            finish()
            false
        })
    }

    override fun initLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {
        splashDialog?.setOnKeyListener(object : SplashDialog.onKeyListener {
            override fun onClickOk() {
                SPUtil.putIsFirstStartApp(false)
                App.app.initSdks()
                startActivity(Intent(this@SplashAct, MainActivity::class.java))
                splashDialog!!.dismiss()
                finish()
            }

            override fun onClickCancel() {
                finish()
            }
        })
    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActivitySplashBinding, viewMode: SplashActVM) {
        selfVM.getConfiData()
    }

}