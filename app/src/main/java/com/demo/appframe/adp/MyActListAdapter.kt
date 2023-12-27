package com.demo.appframe.adp

import android.widget.ImageView
import android.widget.Switch
import com.demo.appframe.R
import com.demo.appframe.beans.MyActFunListItemBean
import com.demo.appframe.util.SPUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MyActListAdapter(data: MutableList<MyActFunListItemBean>?) :
    BaseQuickAdapter<MyActFunListItemBean, BaseViewHolder>(R.layout.item_myact_list, data) {
    override fun convert(holder: BaseViewHolder, item: MyActFunListItemBean) {
        holder.setText(R.id.tv_left_text, item.text)
        val ivres = holder.getView<ImageView>(R.id.iv_icon)
        ivres.setBackgroundResource(item.icon)
        holder.setVisible(R.id.iv_arr, item.showArr).setVisible(R.id.switch_tip, item.showCheckBox)
        holder.getView<Switch>(R.id.switch_tip).isChecked = item.switchChecked
        if (item.showCheckBox) {
            holder.getView<Switch>(R.id.switch_tip)
                .setOnCheckedChangeListener { group, checkedId -> SPUtil.putMessagePushState(group.isChecked) }
        }
    }

}

