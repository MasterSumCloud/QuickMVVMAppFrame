package com.demo.appframe.act

import android.view.View
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.databinding.ActAboutBinding
import com.demo.appframe.vm.AboutActVM

class AboutAct : BaseVMActivity<AboutActVM, ActAboutBinding>() {
    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        getTitleBar().titleText = "关于我们"
        setStateBarColor(false, R.color.black_17171f)
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_about
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {

    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActAboutBinding, viewMode: AboutActVM) {
        viewBinding.vm = viewMode
        selfVM.versionTx()
    }
}