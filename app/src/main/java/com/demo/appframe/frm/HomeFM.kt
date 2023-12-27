package com.demo.appframe.frm

import android.view.View
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.databinding.FragmentHomeBinding
import com.demo.appframe.vm.NoViewModel

class HomeFM : BaseVMFragment<NoViewModel, FragmentHomeBinding>() {
    override fun bindVBM(viewBinding: FragmentHomeBinding, viewMode: NoViewModel) {

    }

    override fun initSelfConfig() {

    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {

    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

}