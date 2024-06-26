package com.demo.appframe.act

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.appframe.R
import com.demo.appframe.adp.VideoAndImageAdapter
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.core.Constent
import com.demo.appframe.databinding.ActViFileAllBinding
import com.demo.appframe.ext.init
import com.demo.appframe.ext.toastShort
import com.demo.appframe.util.GeneralUtil
import com.demo.appframe.vm.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AllVIFileAct : BaseVMActivity<NoViewModel, ActViFileAllBinding>() {
    private var typeOfAct: Int = 0
    private val allFileAdapter = VideoAndImageAdapter()
    override fun initSelfConfig() {
        setStateBarColor(false, R.color.black_17171f)
        getTitleBar().setRightTitleText("确定")
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_vi_file_all
    }

    override fun initSelfViews() {
        selfVB.rvFiles.init(allFileAdapter, GridLayoutManager(this, 3))
    }

    override fun initSelfListener() {
        getTitleBar().setRightClickListener(this)
        /*selfVM.allFileData.observe {
            GlobalScope.launch(Dispatchers.Main) {
                allFileAdapter.setList(it)
            }
        }*/
        allFileAdapter.setOnItemClickListener { _, _, position ->
            val item = allFileAdapter.getItem(position)
            item?.let {
                if (!File(item.path).exists()) {
                    "文件不存在了".toastShort()
                    return@setOnItemClickListener
                }
                allFileAdapter.items.forEach {
                    it.select = false
                }
                item.select = true
                allFileAdapter.notifyDataSetChanged()
            }

        }

    }

    override fun bindVBM(viewBinding: ActViFileAllBinding, viewMode: NoViewModel) {
        typeOfAct = intent.getIntExtra(Constent.TITLE_TYPE, 0)
        if (typeOfAct == 1) {
            setTitleText("视频")
        } else {
            setTitleText("照片")
        }
        getAllFiles()
    }

    private fun getAllFiles() {
        showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            val listData = GeneralUtil.getAllVIFiles(this@AllVIFileAct, typeOfAct)
            withContext(Dispatchers.Main) {
                allFileAdapter.submitList(listData)
            }
//            selfVM.allFileData.set(listData)
        }
        disLoading()
    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {
        when (v?.id) {
            R.id.tv_right_title -> {
                val selectList = allFileAdapter.items.filter { it.select }
                if (selectList.isNotEmpty()) {
                    setResult(RESULT_OK, Intent().setData(Uri.fromFile(File(selectList[0].path))))
                    finish()
                }
            }
        }
    }


    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}