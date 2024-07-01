package com.demo.appframe.ext

import android.content.Context
import android.content.Intent
import com.demo.appframe.App


fun Context.startActivityJuageLogin(cls: Class<*>?) {
    if (App.isLogin) {
        startActivity(Intent(this, cls))
    } else {
        App.app.goLogin()
    }
}

fun Context.startActivity(cls: Class<*>?) {
    val intent = Intent(this, cls)
    startActivity(intent)
}

fun Context.startActivityWithIntent(intent: Intent?) {
    if (intent != null) {
        startActivity(intent)
    }
}