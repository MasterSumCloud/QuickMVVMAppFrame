package com.demo.appframe.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.demo.appframe.R

class TitleBar : RelativeLayout, View.OnClickListener {
    private var mContext: Context?
    private lateinit var mView: View
    private var mLeftClick: View? = null
    private var mTvTitleView: TextView? = null
    private var mTvRightTitle: TextView? = null
    private var mRlRightBtnClick: View? = null
    private var mIvRightImage: ImageView? = null
    private var mIvLeftArrImage: ImageView? = null

    constructor(context: Context?) : super(context) {
        mContext = context
        initView()
        initListener()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initView()
        initListener()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        mContext = context
        initView()
        initListener()
    }

    private fun initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.widgit_title_bar, this)
        mLeftClick = mView.findViewById(R.id.rl_left_btn_click)
        mTvTitleView = mView.findViewById(R.id.tv_title)
        mTvRightTitle = mView.findViewById(R.id.tv_right_title)
        mIvLeftArrImage = mView.findViewById(R.id.iv_left_image)
        mRlRightBtnClick = mView.findViewById(R.id.rl_right_btn_click)
        mIvRightImage = mView.findViewById(R.id.iv_right_image)
        setTitleBarColorType(TITLE_BAR_DARk)
    }

    private fun initListener() {
        mLeftClick!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rl_left_btn_click -> if (mContext != null) {
                val act = mContext as AppCompatActivity
                act.onBackPressed()
            }
        }
    }

    fun setTitleBackgroundColor(color: Int) {
        findViewById<View>(R.id.rl_root_view).setBackgroundColor(ContextCompat.getColor(mContext!!, color))
    }

    var titleText: String?
        get() = mTvTitleView!!.text.toString()
        set(title) {
            mTvTitleView!!.text = title
        }

    fun setRightTitleText(titleText: String?) {
        mTvRightTitle!!.text = titleText
        mTvRightTitle!!.visibility = VISIBLE
        mRlRightBtnClick!!.visibility = GONE
    }

    fun setRightImageRes(res: Int) {
        mIvRightImage!!.setBackgroundResource(res)
        mTvRightTitle!!.visibility = GONE
        mRlRightBtnClick!!.visibility = VISIBLE
    }

    fun setRightClickListener(l: OnClickListener?) {
        mTvRightTitle!!.setOnClickListener(l)
        mRlRightBtnClick!!.setOnClickListener(l)
    }

    /**
     * @see TITLE_BAR_DARk
     * @see TITLE_BAR_BLUE
     */
    fun setTitleBarColorType(type: Int) {
        when (type) {
            TITLE_BAR_DARk -> {
                mTvTitleView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mTvRightTitle!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mIvLeftArrImage!!.setBackgroundResource(R.mipmap.arr_left_white)
                setTitleBackgroundColor(R.color.black_17171f)
            }

            TITLE_BAR_BLUE -> {
                mTvTitleView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mTvRightTitle!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mIvLeftArrImage!!.setBackgroundResource(R.mipmap.arr_left_white)
                setTitleBackgroundColor(R.color.blue_B1ECFE)
            }

            TITLE_BAR_BLACK -> {
                mTvTitleView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mTvRightTitle!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                mIvLeftArrImage!!.setBackgroundResource(R.mipmap.arr_left_white)
                setTitleBackgroundColor(R.color.black)
            }
        }
    }

    fun setLeftBackRes(res: Int) {
        if (res == 0) {
            mLeftClick!!.visibility = GONE
        } else {
            mIvLeftArrImage!!.setBackgroundResource(res)
        }
    }

    companion object {
        const val TITLE_BAR_DARk = 1
        const val TITLE_BAR_BLUE = 2
        const val TITLE_BAR_BLACK = 3
    }
}