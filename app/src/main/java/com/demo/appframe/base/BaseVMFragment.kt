package com.demo.appframe.base

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.demo.appframe.R
import com.demo.appframe.widget.TitleBar
import java.lang.reflect.ParameterizedType

abstract class BaseVMFragment<VM : BaseViewModel, VB : ViewDataBinding> : BaseFragment() {
    private lateinit var viewModel: VM
    private lateinit var mBinding: VB
    private lateinit var mRootContainer: ViewGroup
    private lateinit var mContentView: View
    private lateinit var mEmptyView: View
    private var mEmptyText: TextView? = null
    private var showTitleBarLay = false
    private var mTitleBar: TitleBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = initView(inflater, container)
        createViewModel()
        bindVBM(mBinding, viewModel)
        return root
    }

    fun initView(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        mRootContainer = inflater.inflate(R.layout.fragment_base, container, false) as ViewGroup
        mEmptyView = mRootContainer.findViewById<View>(R.id.empty_base_fm)
        mEmptyText = mRootContainer.findViewById<TextView>(R.id.tv_empty_text)
        mTitleBar = mRootContainer.findViewById<TitleBar>(R.id.title_bar)

        if (initLayoutResId() != 0) {
            val selfContView = LayoutInflater.from(baseActivity).inflate(initLayoutResId(), null)
            mContentView = selfContView
            selfContView.layoutParams
            mBinding = DataBindingUtil.bind<VB>(selfContView)!!
            mBinding.lifecycleOwner = this

            val contentLayoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            selfContView.layoutParams = contentLayoutParams
            mRootContainer.addView(selfContView)
        }
        return mRootContainer
    }

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[0] as Class<VM>
            viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)[cls]
            registerUiChange()
        }
    }

    private fun registerUiChange() {
        //显示弹窗
        viewModel.loadingChange.showDialog.observe(viewLifecycleOwner) {
            showLoading()
        }
        //关闭弹窗
        viewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner) {
            disLoading()
        }
    }

    fun getmTitleBar(): TitleBar? {
        return mTitleBar
    }

    fun setShowTitleBar(showTitleBar: Boolean) {
        showTitleBarLay = showTitleBar
        if (mTitleBar != null) {
            mTitleBar?.setVisibility(if (showTitleBar) View.VISIBLE else View.GONE)
        }
    }

    fun showEmptyView(emptyText: String?) {
        var emptyText = emptyText
        if (TextUtils.isEmpty(emptyText)) {
            emptyText = "抱歉~！没有查到数据哦"
        }
        mContentView.visibility = View.GONE
        mEmptyView.visibility = View.VISIBLE
        mEmptyText?.setText(emptyText)
    }

    fun showContentView() {
        mContentView.visibility = View.VISIBLE
        mEmptyView.visibility = View.GONE
    }

    val selfVB: VB get() = mBinding
    val selfVM: VM get() = viewModel

    protected abstract fun bindVBM(viewBinding: VB, viewMode: VM)
}