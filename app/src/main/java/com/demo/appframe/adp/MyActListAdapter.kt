package com.demo.appframe.adp

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.demo.appframe.R
import com.demo.appframe.beans.MyActFunListItemBean
import com.demo.appframe.ext.loadNetImage
import com.demo.appframe.util.SPUtil

class MyActListAdapter() : BaseMultiItemAdapter<MyActFunListItemBean>() {

    init {
        addItemType(1, object : OnMultiItemAdapterListener<MyActFunListItemBean, QuickViewHolder> {
            override fun onBind(holder: QuickViewHolder, position: Int, item: MyActFunListItemBean?) {
                item?.myfmHeaderData?.let {
                    val headItem = item.myfmHeaderData
                    holder.getView<ImageView>(R.id.imageView).loadNetImage(headItem!!.headerImageUrl, true)
                    holder.setText(R.id.textView3, headItem.userName).setText(R.id.textView39, headItem.userId)
                        .setText(R.id.tvVipEndTime, headItem.vipTime).setText(R.id.textView5, headItem.openVip)
                }
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
                return QuickViewHolder(R.layout.header_of_my, parent)
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                return true
            }
        }).addItemType(2, object : OnMultiItemAdapterListener<MyActFunListItemBean, QuickViewHolder> {
            override fun onBind(holder: QuickViewHolder, position: Int, item: MyActFunListItemBean?) {
                item?.let {
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

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
                return QuickViewHolder(R.layout.item_myact_list, parent)
            }
        }).onItemViewType { posi, list ->
            if (posi == 0) {
                1
            } else {
                2
            }
        }
    }
}

