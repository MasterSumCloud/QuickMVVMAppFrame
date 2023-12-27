package com.demo.appframe.widget

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.demo.appframe.R

class BottomTabView : LinearLayout, View.OnClickListener {
    private var mContext: Context
    private var mIvTabHome: ImageView? = null
    private var mIvTabVip: ImageView? = null
    private var mIvTabMy: ImageView? = null
    private var mCurrentPagePosition = 1
    private var mTvTabHome: TextView? = null
    private var mTvTabVip: TextView? = null
    private var mTvTabMy: TextView? = null
    private var mTabChangeListener: TabChangeListener? = null

    constructor(context: Context) : super(context) {
        mContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initView()
    }

    private fun initView() {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.act_main_btm_tabs, this)
        val rlTabHome = rootView.findViewById<RelativeLayout>(R.id.rl_tab_home)
        mIvTabHome = rootView.findViewById(R.id.iv_tab_home)
        mTvTabHome = rootView.findViewById(R.id.tv_tab_home)
        val rlTabVip = rootView.findViewById<RelativeLayout>(R.id.rl_tab_vip)
        mIvTabVip = rootView.findViewById(R.id.iv_tab_vip)
        mTvTabVip = rootView.findViewById(R.id.tv_tab_vip)
        val rlTabMy = rootView.findViewById<RelativeLayout>(R.id.rl_tab_my)
        mIvTabMy = rootView.findViewById(R.id.rv_tab_my)
        mTvTabMy = rootView.findViewById(R.id.tv_tab_my)
        rlTabHome.setOnClickListener(this)
        rlTabVip.setOnClickListener(this)
        rlTabMy.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rl_tab_home -> if (mCurrentPagePosition != 1) {
                selectItem(1)
            }
            R.id.rl_tab_vip -> if (mCurrentPagePosition != 2) {
                selectItem(2)
            }
            R.id.rl_tab_my -> if (mCurrentPagePosition != 3) {
                selectItem(3)
            }
        }
    }

    fun selectItem(position: Int) {
        mCurrentPagePosition = position
        if (mTabChangeListener != null) {
            mTabChangeListener!!.onTabChangeListener(position)
        }
        setTabTextColor(position)
        setIvBg(position)
    }

    private fun setTabTextColor(position: Int) {
        setTextColor(1==position, mTvTabHome)
        setTextColor(2==position, mTvTabVip)
        setTextColor(3==position, mTvTabMy)
    }

    private fun setTextColor(select: Boolean, tv: TextView?) {
        if (select) {
            tv!!.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
            tv.typeface = boldTypeface
        } else {
            tv!!.setTextColor(ContextCompat.getColor(mContext, R.color.gray_9BA0A2))
            val boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            tv.typeface = boldTypeface
        }
    }

    private fun setIvBg(position: Int) {
        if (position == 1) {
            mIvTabHome!!.setBackgroundResource(R.mipmap.icon_main_home_s)
            mIvTabVip!!.setBackgroundResource(R.mipmap.icon_main_vip_n)
            mIvTabMy!!.setBackgroundResource(R.mipmap.icon_main_my_n)
        } else if (position == 2) {
            mIvTabHome!!.setBackgroundResource(R.mipmap.icon_main_home_n)
            mIvTabVip!!.setBackgroundResource(R.mipmap.icon_main_vip_s)
            mIvTabMy!!.setBackgroundResource(R.mipmap.icon_main_my_n)
        } else if (position == 3) {
            mIvTabHome!!.setBackgroundResource(R.mipmap.icon_main_home_n)
            mIvTabVip!!.setBackgroundResource(R.mipmap.icon_main_vip_n)
            mIvTabMy!!.setBackgroundResource(R.mipmap.icon_main_my_s)
        }
    }

    interface TabChangeListener {
        fun onTabChangeListener(position: Int)
    }

    fun setTabChangeListener(tabChangeListener: TabChangeListener?) {
        mTabChangeListener = tabChangeListener
    }
}
