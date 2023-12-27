package com.demo.appframe.ext

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.GsonUtils
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.base.BaseVMFragment
import com.demo.appframe.util.GeneralUtil
import com.demo.appframe.util.Tos
import com.chad.library.adapter.base.BaseQuickAdapter

fun BaseVMActivity<*, *>.setViewClicks(vararg view: View) {
    for (v in view) {
        v.setOnClickListener(this)
    }
}

fun BaseVMFragment<*, *>.setViewClicks(vararg view: View) {
    for (v in view) {
        v.setOnClickListener(this)
    }
}

fun BaseVMActivity<*, *>.getColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Fragment.setViewClicks(vararg view: View, onClick: View.OnClickListener) {
    for (v in view) {
        v.setOnClickListener(onClick)
    }
}

fun BaseQuickAdapter<*, *>.getColor(color: Int): Int {
    return ContextCompat.getColor(context, color)
}

fun String?.isNotEmptyOrNull(): Boolean {
    if ("null".equals(this)) {
        return false
    }
    return !TextUtils.isEmpty(this)
}

fun String?.isEmptyOrNull(): Boolean {
    return TextUtils.isEmpty(this)
}

fun <T> String?.toJsonString(bean: T) {
    GsonUtils.toJson(bean)
}

fun <T> String?.fromJson(type: Class<T>): T? {
    if (this.isNotEmptyOrNull() && this?.startsWith("{") == true) {
        return GsonUtils.fromJson(this, type)
    } else {
        return null
    }
}

fun String?.toastShort() {
    if (this.isNotEmptyOrNull()) {
        Tos.showToastShort(this)
    }
}

fun String?.toastLong() {
    if (this.isNotEmptyOrNull()) {
        Tos.showToastLong(this)
    }
}

fun String?.nozero(): String? {
    if (this != null && this.contains(".0")) {
        return this.replace(".0", "")
    } else {
        return this
    }
}

fun Fragment.getColor(res: Int): Int? {
    return context?.getColor(res)
}

fun ViewPager2.initAdapter(fragmentActivity: FragmentActivity, fragments: MutableList<Fragment>) {
    adapter = object : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        override fun getItemCount(): Int {
            return fragments.size
        }
    }
}


fun Int.dp2pxOfApp(): Float {
    return GeneralUtil.dpTPointOfApp(this.toFloat())
}

fun Float.dp2pxOfApp(): Float {
    return GeneralUtil.dpTPointOfApp(this)
}