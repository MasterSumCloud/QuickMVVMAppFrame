package com.demo.appframe.frm

import android.view.View
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.databinding.FragmentHomeBinding
import com.demo.appframe.ext.setViewClicks
import com.demo.appframe.util.GeneralUtil
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
        setViewClicks(selfVB.tvHome)
    }

    override fun initSelfListener() {

    }

    override fun singeClick(v: View?) {
        when (v) {
            selfVB.tvHome -> {
                GeneralUtil.openFileManager(this)
            }
        }
    }

    override fun normalClick(v: View?) {

    }

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

}