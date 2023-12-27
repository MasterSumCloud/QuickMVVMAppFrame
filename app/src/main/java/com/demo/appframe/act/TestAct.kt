package com.demo.appframe.act

import android.view.View
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.databinding.ActTestBinding
import com.demo.appframe.vm.TestActVM


class TestAct : BaseVMActivity<TestActVM, ActTestBinding>() {


    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        setStateBarColor(false, R.color.black_17171f)
    }


    override fun initLayoutResId(): Int {
        return R.layout.act_test
    }

    override fun initSelfViews() {
    }

    override fun initSelfListener() {

    }


    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActTestBinding, viewMode: TestActVM) {
    }


}