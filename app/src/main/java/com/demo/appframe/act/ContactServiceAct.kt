package com.demo.appframe.act

import android.view.View
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.databinding.ActServiceBinding
import com.demo.appframe.vm.ContactActVM

class ContactServiceAct : BaseVMActivity<ContactActVM, ActServiceBinding>() {
    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        getTitleBar().titleText = "联系客服"
        setStateBarColor(false, R.color.black_17171f)
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_service
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {

    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActServiceBinding, viewMode: ContactActVM) {
        viewBinding.vm = viewMode
        selfVM.getContact()
    }
}