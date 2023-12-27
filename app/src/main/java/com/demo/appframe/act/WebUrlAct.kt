package com.demo.appframe.act

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.demo.appframe.R
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.base.BaseVMActivity
import com.demo.appframe.base.BaseViewModel
import com.demo.appframe.core.Constent
import com.demo.appframe.databinding.ActWebUrlBinding

class WebUrlAct : BaseVMActivity<BaseViewModel, ActWebUrlBinding>() {
    override fun onSelfMessageEvent(messageEvent: BaseMessageEvent?) {

    }

    override fun initSelfConfig() {
        val title = intent.getStringExtra(Constent.TITLE_TEXT)
        setTitleText(title)
        setStateBarColor(false, R.color.black_17171f)
        selfVB.web.webViewClient = WebViewClient()
        val webUrl = intent.getStringExtra(Constent.WEB_URL)
        selfVB.web.loadUrl(webUrl!!)
        val settings = selfVB.web.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        val windowType = intent.getIntExtra(Constent.TITLE_TYPE, 1)
        if (windowType == 2) {
            getTitleBar().setBackgroundColor(getColor(R.color.blue_B1ECFE))
        }
        val canshare = intent.getBooleanExtra(Constent.WEB_URL_CAN_SHARE, false)
        if (canshare) {
            getTitleBar().setRightImageRes(R.mipmap.icon_share_white)
        }
    }

    override fun initLayoutResId(): Int {
        return R.layout.act_web_url
    }

    override fun initSelfViews() {

    }

    override fun initSelfListener() {

    }

    override fun singeClick(v: View?) {

    }

    override fun normalClick(v: View?) {

    }

    override fun bindVBM(viewBinding: ActWebUrlBinding, viewMode: BaseViewModel) {

    }
}