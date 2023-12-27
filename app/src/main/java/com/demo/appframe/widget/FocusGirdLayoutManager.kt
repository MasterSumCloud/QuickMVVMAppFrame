package com.demo.appframe.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 为了解决recycle人View上添加的headView 中的EditText等控件获取了焦点导致RecyclerView 莫名滚动
 */
class FocusGirdLayoutManager(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView, child: View, rect: Rect, immediate: Boolean
    ): Boolean {
        return false
    }

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean {
        return false
    }
}
