package com.demo.appframe.event

import com.demo.appframe.base.BaseMessageEvent

class LoginMessageEvent(tag: String?, message: String?, type: Int) : BaseMessageEvent(tag,message) {
    var type: Int? = type
}