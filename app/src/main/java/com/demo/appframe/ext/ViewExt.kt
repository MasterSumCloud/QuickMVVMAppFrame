package com.demo.appframe.ext

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.appframe.util.GlideUtil
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun RecyclerView.init(
    adapter: RecyclerView.Adapter<*>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    hasFixedSize: Boolean = true
): RecyclerView {
    this.layoutManager = layoutManager
    setHasFixedSize(hasFixedSize)
    setAdapter(adapter)
    return this
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadNetImage(url: String?, round: Boolean = false, corner: Int = 0) {
    if (corner > 0) {
        GlideUtil(this.context).dspImageCorner(url, this, corner)
    } else {
        if (round) {
            GlideUtil(this.context).dspImageRound(url, this)
        } else {
            GlideUtil(this.context).dspImage(url, this)
        }
    }
}

fun ImageView.loadNetImageRound(url: String?, corner: Int, type: RoundedCornersTransformation.CornerType) {
    GlideUtil(this.context).dspImageCornerCustom(url, this, corner, type)
}