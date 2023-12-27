package com.demo.appframe.base

open class BaseMessageEvent {
    var tag: String? = null
    var message: String? = null
    var isBooleanValue = false

    constructor() {}
    constructor(tag: String?, message: String?) {
        this.tag = tag
        this.message = message
    }

    constructor(tag: String?, booleanValue: Boolean) {
        this.tag = tag
        isBooleanValue = booleanValue
    }
}