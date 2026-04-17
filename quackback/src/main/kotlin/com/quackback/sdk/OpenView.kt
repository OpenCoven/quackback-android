package com.quackback.sdk

/** A specific view the widget can open to, passed to [Quackback.open]. */
enum class OpenView(val value: String) {
    HOME("home"),
    NEW_POST("new-post"),
    CHANGELOG("changelog"),
}
