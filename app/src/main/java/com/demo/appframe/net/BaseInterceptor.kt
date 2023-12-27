package com.demo.appframe.net

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.demo.appframe.App
import okhttp3.Interceptor
import okhttp3.Response


class BaseInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
//        chain.request().url.toString().logd("请求头")
        val headers = mutableMapOf<String, String?>()
        headers["appVersion"] = AppUtils.getAppVersionName()
        headers["devicePlatform"] = "Android"
//        headers["deviceNo"] = App.deviceId
        headers["channelName"] = App.appChannel
        headers["phoneType"] = DeviceUtils.getManufacturer()
        headers["token"] = App.token
        headers["Content-Type"] = "application/json; charset=utf-8"
        headers["Connection"] = "keep-alive"
        val keys: Set<String> = headers.keys
        for (headerKey in keys) {
            if (headers[headerKey]?.isNotEmpty() == true) {
                builder.addHeader(headerKey, headers[headerKey]!!).build()
            }
        }
        return chain.proceed(builder.build())
    }
}