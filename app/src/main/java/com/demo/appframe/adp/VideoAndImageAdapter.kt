package com.demo.appframe.adp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.demo.appframe.R
import com.demo.appframe.beans.MediaFile
import com.demo.appframe.util.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class VideoAndImageAdapter(data: MutableList<MediaFile>?) :
    BaseQuickAdapter<MediaFile, BaseViewHolder>(R.layout.item_video_image_file, data) {
    override fun convert(holder: BaseViewHolder, item: MediaFile) {
        val ivCover = holder.getView<ImageView>(R.id.ivCover)
        if (item.mediaType == 1) {
            GlideUtil(context).dspVideo1Image(item.path, ivCover)
        } else {
            GlideUtil(context).dspImage(item.path, ivCover)
        }
        val tvDura = holder.getView<TextView>(R.id.tvDuration)
        if (item.duration > 0) {
            tvDura.visibility = View.VISIBLE
            tvDura.text = calculateTime(item.duration.div(1000))
        } else {
            tvDura.visibility = View.GONE
        }
        val ivSelect = holder.getView<ImageView>(R.id.ivSelect)
        if (item.select) {
            ivSelect.setImageResource(R.mipmap.vi_select)
        } else {
            ivSelect.setImageResource(R.mipmap.vi_nselect)
        }
    }

    private fun calculateTime(time: Long): String {
        val minute: Long
        val second: Long
        return if (time >= 60) {
            minute = time / 60
            second = time % 60
            //分钟在0~9
            if (minute < 10) {
                //判断秒
                if (second < 10) {
                    "0$minute:0$second"
                } else {
                    "0$minute:$second"
                }
            } else {
                //分钟大于10再判断秒
                if (second < 10) {
                    "$minute:0$second"
                } else {
                    "$minute:$second"
                }
            }
        } else {
            second = time
            if (second in 0..9) {
                "00:0$second"
            } else {
                "00:$second"
            }
        }
    }
}