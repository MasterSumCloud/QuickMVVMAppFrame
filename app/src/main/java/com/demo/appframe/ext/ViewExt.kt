package com.demo.appframe.ext

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.appframe.util.GlideUtil

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

fun ImageView.dspImage(url: String?) {
    GlideUtil(this.context).dspImage(url, this)
}