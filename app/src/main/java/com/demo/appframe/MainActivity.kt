package com.demo.appframe

import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.core.AliAuthLoginConfig
import com.demo.appframe.core.Constent
import com.demo.appframe.databinding.ActMainBinding
import com.demo.appframe.ext.initAdapter
import com.demo.appframe.ext.observe
import com.demo.appframe.frm.HomeFM
import com.demo.appframe.frm.MyFM
import com.demo.appframe.frm.VipFM
import com.demo.appframe.util.SPUtil
import com.demo.appframe.vm.MainVM
import com.demo.appframe.widget.BottomTabView

class MainActivity : BaseVMActivity<MainVM, ActMainBinding>() {
    private lateinit var fragmentsList: MutableList<Fragment>

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {
        when (messageEvent?.tag) {
            Constent.HOME_TAB_CHANGE -> {
                messageEvent.message?.toInt()?.let { setPage(it) }
            }
        }
    }

    override fun initSelfConfig() {
        laterUnRegisetEventBus = true
        hideTitleBar()
        BarUtils.setNavBarColor(this, getColor(R.color.black_212330))
        App.app.setMainCtx(this)
        //阿里一键登录
        if (App.app.mPhoneNumberAuthHelper != null) {
            AliAuthLoginConfig(this, App.app.mPhoneNumberAuthHelper!!)
        }
        fragmentsList = mutableListOf<Fragment>().apply {
            add(HomeFM())
            add(VipFM())
            add(MyFM())
        }

        selfVB.vpContainer.run {
            initAdapter(this@MainActivity, fragmentsList)
            offscreenPageLimit = 3
            isUserInputEnabled = false //禁用左右滑动
        }
        setPage(0)
    }


    override fun initLayoutResId(): Int {
        return R.layout.act_main
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {
        selfVB.btmTabView.setTabChangeListener(object : BottomTabView.TabChangeListener {
            override fun onTabChangeListener(position: Int) {
                val select = position - 1
                when (select) {
                    0 -> {}
                    1 -> {}
                    2 -> {}
                    3 -> {}
                }
                selfVB.vpContainer.currentItem = select
            }
        })

        selfVM.notifyUserInfo.observe {
            fragmentsList.forEach {
                if (it is MyFM) {
                    val myfm = it as MyFM
                    myfm.selfVM.setLoginInfo()
                }
            }
        }

    }

    fun setPage(position: Int) {
        selfVB.btmTabView.selectItem(position + 1)
    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActMainBinding, viewMode: MainVM) {
        selfVM.getBasicUserInfo()
        if (!SPUtil.getHasUploadADZZ()) {
            selfVM.upgd(1, null, null)
        } else {
            selfVM.upgd(4, null, null)
        }

    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        selfVM.getBasicUserInfo()
    }

    override fun onLoginOut() {
        super.onLoginOut()
        outUser()
    }

    private fun outUser() {
        SPUtil.putLoginInfoBean(null)
        SPUtil.putToken(null)
        App.isLogin = false
        App.isVip = false
        App.token = null
        App.myActInfo = null
    }
}