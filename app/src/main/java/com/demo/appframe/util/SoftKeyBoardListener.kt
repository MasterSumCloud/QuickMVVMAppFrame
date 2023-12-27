package com.demo.appframe.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

/**
 * eagle
 */
//源码
class SoftKeyBoardListener(activity: Activity) {
    private val mRootView: View //activity的根视图
    var mRootViewVisibleHeight = 0 //纪录根视图的显示高度
    private var mOnSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    init {
        //获取activity的根视图
        mRootView = activity.window.decorView

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener { //获取当前根视图在屏幕上显示的大小
            val r = Rect()
            mRootView.getWindowVisibleDisplayFrame(r)
            val visibleHeight = r.height()
            if (mRootViewVisibleHeight == 0) {
                mRootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (mRootViewVisibleHeight == visibleHeight) {
                return@OnGlobalLayoutListener
            }

            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (mRootViewVisibleHeight - visibleHeight > 200) {
                if (mOnSoftKeyBoardChangeListener != null) {
                    mOnSoftKeyBoardChangeListener!!.keyBoardShow(mRootViewVisibleHeight - visibleHeight)
                }
                mRootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - mRootViewVisibleHeight > 200) {
                if (mOnSoftKeyBoardChangeListener != null) {
                    mOnSoftKeyBoardChangeListener!!.keyBoardHide(0)
                }
                mRootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
        })
    }

    private fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
        mOnSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)
        fun keyBoardHide(height: Int)
    }

    companion object {
        fun setListener(
            activity: Activity, onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener
        ) {
            val softKeyBoardListener = SoftKeyBoardListener(activity)
            softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener)
        }
    }
}