package com.demo.appframe.net

import android.util.Log
import com.demo.appframe.ext.logHttp
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class HttpLogInterceptor : Interceptor {

    private val TAG = "请求头"
    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        var body: String? = null
        requestBody?.let {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody.contentType()
            contentType?.let {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset!!)
        }


//        val startNs = System.nanoTime()
        val response = chain.proceed(request)
//        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body

        val source = responseBody!!.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer

        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        contentType?.let {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                e.message?.let { it1 -> Log.e(TAG, it1) }
            }
        }
        val rBody = buffer.clone().readString(charset!!)
//        val cnBody = UnicodeUtils.decode(rBody)

        StringBuilder().apply {
            append("发送请求: method：").append(request.method)
            append("\nurl:").append(request.url)
            append("\n请求头:").append(request.headers)
            append("\n请求参数:").append(body)
            append("\n收到响应: code:").append(response.code)
            append("\nResponse:").append(rBody)
        }.toString().logHttp()

        return response
    }

}