package com.demo.appframe.vm

import androidx.databinding.ObservableField
import com.demo.appframe.base.BaseViewModel

class ContactActVM : BaseViewModel() {

    val qq = ObservableField<String>()
    val telephone = ObservableField<String>()


    fun getContact() {
        /*request({ apiService.getContact() }, {
            qq.set("联系QQ：${it?.qq}")
            telephone.set("联系电话：${it?.telephone}")
        }, {})*/
    }
}