package com.demo.appframe.net

import android.net.ParseException
import com.demo.appframe.App
import com.demo.appframe.event.LoginMessageEvent
import com.demo.appframe.core.Constent
import com.demo.appframe.util.SPUtil
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                    return ex
                }

                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR, e)
                    return ex
                }

                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                    return ex
                }

                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR, e)
                    return ex
                }

                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is AppException -> {
                    if (it.errCode == 4003) {
                        outUser()
                    }
                    return it
                }

                else -> {
                    ex = AppException(Error.UNKNOWN, e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN, e)
        return ex
    }

    private fun outUser() {
        SPUtil.putLoginInfoBean(null)
        SPUtil.putToken(null)
        App.isLogin = false
        App.isVip = false
        App.token = null
        App.myActInfo = null
        EventBus.getDefault().post(LoginMessageEvent(Constent.LOGIN_OUT, null, 1))
    }
}