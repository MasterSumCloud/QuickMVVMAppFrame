package com.demo.appframe.frm

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.ClipboardUtils
import com.demo.appframe.App
import com.demo.appframe.R
import com.demo.appframe.act.*
import com.demo.appframe.adp.MyActListAdapter
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.core.Constent
import com.demo.appframe.core.UCS
import com.demo.appframe.databinding.FragmentMyBinding
import com.demo.appframe.databinding.HeaderOfMyBinding
import com.demo.appframe.ext.init
import com.demo.appframe.ext.setViewClicks
import com.demo.appframe.ext.toastShort
import com.demo.appframe.vm.FrmMyVM
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

class MyFM : BaseVMFragment<FrmMyVM, FragmentMyBinding>() {
    private val listAdapter: MyActListAdapter = MyActListAdapter(null)


    override fun initSelfConfig() {
        selfVM.setLoginInfo()
    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_my
    }

    override fun initSelfViews() {
        listAdapter.setNewInstance(selfVM.createFuncListData())
        val headerView = LayoutInflater.from(baseActivity).inflate(R.layout.header_of_my, null)
        val headerBinding = DataBindingUtil.bind<HeaderOfMyBinding>(headerView)
        listAdapter.addHeaderView(headerBinding?.root!!)
        headerView.apply {
            setViewClicks(
                findViewById(R.id.imageView),
                findViewById(R.id.view),
                findViewById(R.id.textView39),
                findViewById(R.id.textView3)
            )
        }
        headerBinding.vm = selfVM
        selfVB.rvMy.init(listAdapter)
    }

    override fun initSelfListener() {
        listAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = listAdapter.getItem(position)
                when (item.text) {
                    "联系客服" -> {
                        startActivityJuageLogin(ContactServiceAct::class.java)
                    }

                    "用户协议" -> {
                        val intent = Intent(baseActivity, WebUrlAct::class.java)
                        intent.putExtra(Constent.WEB_URL, UCS.YHXY_URL)
                        intent.putExtra(Constent.TITLE_TEXT, "用户协议")
                        startActivity(intent)
                    }

                    "隐私协议" -> {
                        val intent = Intent(baseActivity, WebUrlAct::class.java)
                        intent.putExtra(Constent.WEB_URL, UCS.YSZC_URL)
                        intent.putExtra(Constent.TITLE_TEXT, "隐私协议")
                        startActivity(intent)
                    }

                    "设置" -> {
                        startActivity(SettingAct::class.java)
                    }

                    "测试" -> {
                        startActivity(TestAct::class.java)
                    }
                }
            }
        })
    }

    override fun singeClick(v: View?) {
        when (v?.id) {
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


}