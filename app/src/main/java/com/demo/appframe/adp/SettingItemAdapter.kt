package com.demo.appframe.adp

import com.demo.appframe.R
import com.demo.appframe.beans.SetBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class SettingItemAdapter(data: MutableList<SetBean>?) :
    BaseQuickAdapter<SetBean, BaseViewHolder>(R.layout.item_setting, data) {
    override fun convert(holder: BaseViewHolder, item: SetBean) {
        holder.setText(R.id.tv_left_text, item.text).setText(R.id.tv_right_text, item.cacheSize)
    }
}