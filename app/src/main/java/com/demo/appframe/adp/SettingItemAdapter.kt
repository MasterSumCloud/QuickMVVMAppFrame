package com.demo.appframe.adp

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.demo.appframe.R
import com.demo.appframe.beans.SetBean

class SettingItemAdapter() :
    BaseQuickAdapter<SetBean, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SetBean?) {
        if (item != null) {
            holder.setText(R.id.tv_left_text, item.text).setText(R.id.tv_right_text, item.cacheSize)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_setting, parent)
    }
}