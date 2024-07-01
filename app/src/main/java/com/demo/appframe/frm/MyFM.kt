package com.demo.appframe.frm

import android.view.View
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.blankj.utilcode.util.ClipboardUtils
import com.chad.library.adapter4.BaseQuickAdapter
import com.demo.appframe.App
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.beans.MyActFunListItemBean
import com.demo.appframe.compose.ui.MyUI
import com.demo.appframe.core.Constent
import com.demo.appframe.databinding.FragmentMyBinding
import com.demo.appframe.ext.observe
import com.demo.appframe.ext.toastShort
import com.demo.appframe.vm.FrmMyVM

class MyFM : BaseVMFragment<FrmMyVM, FragmentMyBinding>(), BaseQuickAdapter.OnItemChildClickListener<MyActFunListItemBean> {
    override fun initSelfConfig() {
        selfVM.setLoginInfo()
    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_my
    }

    override fun initSelfViews() {
        selfVB.myComposeUI.setContent {
            MyUI(
                modifierUI = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            )
        }
    }

    override fun initSelfListener() {
        selfVM.notifyHeader.observe {
            //TODO Update Login State
        }
    }

    override fun singeClick(v: View?) {

    }


    override fun onLoginSuccess() {
        super.onLoginSuccess()
        selfVM.setLoginInfo()
    }

    override fun onLoginOut() {
        super.onLoginOut()
        selfVM.setLoginInfo()
    }

    override fun onResume() {
        super.onResume()
        selfVM.setLoginInfo()
    }


    override fun normalClick(v: View?) {

    }

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {
        when (messageEvent?.tag) {
            Constent.VIP_STATE_INFO_CHANGE -> {
                selfVM.setLoginInfo()
            }
        }
    }

    override fun bindVBM(viewBinding: FragmentMyBinding, viewMode: FrmMyVM) {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<MyActFunListItemBean, *>, v: View, position: Int) {
        when (v.id) {
            R.id.imageView, R.id.textView3 -> {
                if (!App.isLogin) {
                    App.app.goLogin()
                }
            }

            R.id.view -> {
            }

            R.id.textView39 -> {
                ClipboardUtils.copyText((v as TextView).text)
                "复制成功".toastShort()
            }
        }
    }


}