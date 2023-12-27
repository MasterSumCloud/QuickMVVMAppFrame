package com.demo.appframe.base

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.TimeUtils
import com.demo.appframe.event.LoginMessageEvent
import com.demo.appframe.R
import com.demo.appframe.core.UCS
import com.demo.appframe.databinding.ActivityBaseBinding
import com.demo.appframe.ext.visible
import com.demo.appframe.widget.TitleBar
import java.lang.reflect.ParameterizedType

abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewDataBinding> : BaseActivity(),
    View.OnClickListener {
    private lateinit var viewModel: VM
    private lateinit var viewBinding: VB
    private lateinit var mContainerView: FrameLayout
    private lateinit var mRootView: View
    private lateinit var mTitleBarView: TitleBar
    private lateinit var mStateBar: View
    private lateinit var emptyView: View
    private lateinit var tvEmptyText: TextView
    private lateinit var mBtnReload: Button
    private var mLastClickView: View? = null
    private var mLastClickTime: Long = 0
    private var lightMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVMConfig()
        initView()
        registerUiChange()
        bindVBM(viewBinding, viewModel)

        initSelfConfig()
        initSelfViews()
        initSelfListener()
    }

    private fun initVMConfig() {
        createViewModel()
    }

    private fun initView() {
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_base, null)
        val actBaseVm = DataBindingUtil.bind<ActivityBaseBinding>(mRootView)
        mContainerView = mRootView.findViewById<FrameLayout>(R.id.fl_container)
        mTitleBarView = mRootView.findViewById(R.id.tb_base_title)
        mStateBar = mRootView.findViewById(R.id.view_state_bar_height)
        emptyView = mRootView.findViewById(R.id.empty_base)
        tvEmptyText = mRootView.findViewById<TextView>(R.id.tv_empty_text)
        mBtnReload = mRootView.findViewById(R.id.btn_re_load)
        mBtnReload.setOnClickListener(this)
        val layoutParams: ViewGroup.LayoutParams = mStateBar.getLayoutParams()
        layoutParams.height = BarUtils.getStatusBarHeight()
        mStateBar.setLayoutParams(layoutParams)

        if (initLayoutResId() != 0) {
            val selfContView = LayoutInflater.from(this).inflate(initLayoutResId(), null)
            viewBinding = DataBindingUtil.bind<VB>(selfContView)!!
            viewBinding.lifecycleOwner = this
            mContainerView.addView(selfContView)
        }
        setContentView(actBaseVm?.root)
    }

    fun setTitleText(titleText: String?) {
        getTitleBar().titleText = titleText
    }

    fun getTitleBar(): TitleBar {
        return mTitleBarView
    }

    fun hideTitleBar() {
        mTitleBarView.setVisibility(View.GONE)
    }

    fun hideTitleBarAndStateBar(light: Boolean) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.setStatusBarColor(Color.TRANSPARENT)
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, light)
        hideTitleBar()
        setShowStateBarShow(false)
    }

    fun setShowStateBarShow(show: Boolean) {
        mStateBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showEmptyView(emptyText: String?) {
        var emptyTextN = emptyText
        if (TextUtils.isEmpty(emptyText)) {
            emptyTextN = "抱歉~！没有查到数据哦"
        }
        mContainerView.setVisibility(View.GONE)
        emptyView.visibility = View.VISIBLE
        tvEmptyText.setText(emptyTextN)
    }

    fun showContentView() {
        mContainerView.setVisibility(View.VISIBLE)
        emptyView.visibility = View.GONE
    }

    fun hasMainData(): Boolean {
        return mContainerView.getVisibility() == View.VISIBLE
    }

    fun setReloadButtonShowOrNor(visible: Int) {
        mBtnReload.visibility = visible
    }


    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[0] as Class<VM>
            viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)[cls]
        }

    }

    override fun onClick(v: View) {
        val nowMills = TimeUtils.getNowMills()
        if (mLastClickView === v) {
            if (nowMills - mLastClickTime > UCS.TIME_CLICK_INTERVAL) {
                singeClick(v)
                normalClick(v)
            } else {
                normalClick(v)
            }
        } else {
            singeClick(v)
            normalClick(v)
        }
        mLastClickView = v
        mLastClickTime = nowMills
        when (v.id) {
            R.id.btn_re_load -> ReloadData()
        }
    }

    open fun setStateBarColor(light: Boolean, color: Int = 0) {
        if (color != 0) {
            BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, color))
        }
        BarUtils.setStatusBarLightMode(this, light)
        mStateBar.visible()
    }


    private fun registerUiChange() {
        //显示弹窗
        viewModel.loadingChange.showDialog.observe(this) {
            showLoading()
        }
        //关闭弹窗
        viewModel.loadingChange.dismissDialog.observe(this) {
            disLoading()
        }
    }

    override fun onLogin(messageEvent: BaseMessageEvent) {
        super.onLogin(messageEvent)
        val event: LoginMessageEvent = messageEvent as LoginMessageEvent
        selfVM.login(event.type, messageEvent.message)
    }

    fun ReloadData() {}

    //设置必要的配置
    protected abstract fun initSelfConfig()

    //返回Act的布局
    protected abstract fun initLayoutResId(): Int

    //初始化View和Listener
    protected abstract fun initSelfViews()
    protected abstract fun initSelfListener()

    protected abstract fun bindVBM(viewBinding: VB, viewMode: VM)

    abstract fun singeClick(v: View?)
    abstract fun normalClick(v: View?)

    val selfVB: VB get() = viewBinding
    val selfVM: VM get() = viewModel

}