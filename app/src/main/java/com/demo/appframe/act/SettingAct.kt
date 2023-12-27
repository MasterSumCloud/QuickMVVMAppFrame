package com.demo.appframe.act

import android.view.View
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.demo.appframe.R
import com.demo.appframe.adp.SettingItemAdapter
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.databinding.ActSettingBinding
import com.demo.appframe.dialog.TextDialog
import com.demo.appframe.ext.init
import com.demo.appframe.ext.observe
import com.demo.appframe.vm.SettingActVM
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

class SettingAct : BaseVMActivity<SettingActVM, ActSettingBinding>() {

    private val adapterSetting = SettingItemAdapter(null)
    private lateinit var unRegistTxDialog: TextDialog

    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        getTitleBar().titleText = "设置"
        setStateBarColor(false, R.color.black_17171f)
        unRegistTxDialog = TextDialog(this)
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_setting
    }

    override fun initSelfViews() {
        adapterSetting.setNewInstance(selfVM.itemFuncList.get())
        selfVB.recyclerView.init(adapterSetting)
    }

    override fun initSelfListener() {

        adapterSetting.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val setBean = adapterSetting.getItem(position)
                when (setBean.text) {
                    "清理缓存" -> {
                        CacheDiskStaticUtils.clear()
//                        File(GeneralUtil.getCacheOfFilelibtxtPath()).delete()
                        setBean.cacheSize = "0k"
                        adapterSetting.notifyItemChanged(0)
                    }
//                    "检查更新" -> //                        Tos.INSTANCE.showToastShort("已经是最新版本");
                    "关于我们" -> startActivity(AboutAct::class.java)
                    "注销账号" -> {
                        unRegistTxDialog.showTextDialog("您确定注销账号吗？该账户注销后将无法使用！",
                            object : TextDialog.onTextDialogBtnListener {
                                override fun onClickOk() {
                                    selfVM.unRegistUser()
                                }

                                override fun onClickCancel() {

                                }
                            })
                    }
//                    "个性化推送" -> {}
                }
            }
        })

        selfVM.outLoginOrUnregist.observe {
            if (it) {
                finish()
            }
        }
    }


    override fun singeClick(v: View?) {
        when (v?.id) {
            R.id.btn_login_out -> {
                selfVM.unRegistUser()
            }
        }
    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActSettingBinding, viewMode: SettingActVM) {
        viewBinding.vm = viewMode
        selfVM.getFunsList()
    }
}