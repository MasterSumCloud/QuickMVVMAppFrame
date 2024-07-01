package com.demo.appframe.frm

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.databinding.FragmentVipBinding
import com.demo.appframe.vm.NoViewModel

class VipFM : BaseVMFragment<NoViewModel, FragmentVipBinding>() {
    override fun bindVBM(viewBinding: FragmentVipBinding, viewMode: NoViewModel) {

    }

    override fun initSelfConfig() {
        selfVB.composeView.setContent {
            Surface {
                VipUI()
            }
        }
    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_vip
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

    @Composable
    fun VipUI() {
        Box {
            Text(text = "VIP页面", modifier = Modifier.align(Alignment.Center))
        }
    }
}