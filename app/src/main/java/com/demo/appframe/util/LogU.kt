package com.demo.appframe.util

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.demo.appframe.core.Constent
import com.demo.appframe.core.Toggles

object LogU {

    fun d(msg: String?) {
        if (Toggles.isNotShowLog) {
            Log.d(Constent.LOG_TAG, msg, null)
        }
    }

    fun d(tag: String, msg: String?) {
        if (Toggles.isNotShowLog) {
            Log.d(tag, msg, null)
        }
    }

    fun e(msg: String?) {
        if (Toggles.isNotShowLog) {
            LogUtils.e(Constent.LOG_TAG, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (Toggles.isNotShowLog) {
            LogUtils.e(tag, msg)
        }
    }

    fun e(tag: String, msg: Unit) {
        if (Toggles.isNotShowLog) {
            LogUtils.e(tag, msg)
        }
    }

    fun dUrl(msg: String?) {
        if (Toggles.isNotShowLog) {
            LogUtils.dTag("REQUEST_URL__", msg)
        }
    }

    fun dResp(msg: String?) {
        if (Toggles.isNotShowLog) {
            LogUtils.dTag("REPONSE_JSON__", msg)
        }
    }

    fun dMessageEvent(msg: String?) {
        if (Toggles.isNotShowLog) {
            LogUtils.dTag("MESSAGE_EVENT", "传递消息的TAG=" + msg)
        }
    }

    fun dHttp(msg: String?) {
        if (Toggles.isNotShowLog) {
//            LogUtils.dTag("HTTP_", msg)
            msg?.let { Log.d("HTTP_", it) }
        }
    }

    fun dResponse(msg: String?) {
        if (Toggles.isNotShowLog) {
//            LogUtils.dTag("HTTP_", msg)
            msg?.let {
                if (it.length > 4000) {
                    LogUtils.d(it)
                } else {
                    Log.d("请求结果", it)
                }
            }
        }
    }
}