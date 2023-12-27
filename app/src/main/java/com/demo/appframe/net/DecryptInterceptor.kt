package com.demo.appframe.net

import com.demo.appframe.core.UCS
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 解密拦截器
 */
class DecryptInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val response = chain.proceed(request)
        if (url.startsWith(UCS.BASE_URL)) {
            return getResponseBody(response)
        } else {
            return response
        }
    }

    @Throws(Exception::class)
    private fun getResponseBody(response: Response): Response {
        val responseBody = response.body ?: return response
        var respBodyStr = responseBody.string()
//        LogUtils.i("解密前：$respBodyStr")
        /*if (respBodyStr.isNotEmpty() && respBodyStr.startsWith("{")) {
            val jsonBase = GsonUtils.fromJson(respBodyStr, BaseBeanJZ::class.java)
            val dcstrdata = GeneralUtil.dcstrdata(jsonBase.data.toString())
//            LogUtils.i("解密后str=：$dcstrdata")
            val gson =
                GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()
            jsonBase.data = GsonUtils.fromJson(gson, dcstrdata, Any::class.java)
            respBodyStr = GsonUtils.toJson(jsonBase)
//            LogUtils.i("解密后：$respBodyStr")
            respBodyStr.logHttp()
        }*/
        return response.newBuilder().body(respBodyStr.toResponseBody("text/plain".toMediaType()))
            .build()
    }
}