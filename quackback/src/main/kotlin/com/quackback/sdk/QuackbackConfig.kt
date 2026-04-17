package com.quackback.sdk
import android.net.Uri

enum class QuackbackTheme(val value: String) { LIGHT("light"), DARK("dark"), SYSTEM("user") }
enum class QuackbackPosition { BOTTOM_RIGHT, BOTTOM_LEFT }

data class QuackbackConfig(
    val appUrl: String,
    val theme: QuackbackTheme = QuackbackTheme.SYSTEM,
    val placement: QuackbackPosition = QuackbackPosition.BOTTOM_RIGHT,
    val buttonColor: String? = null,
    val locale: String? = null,
) {
    val widgetURL: String get() = Uri.parse(appUrl).buildUpon()
        .path("/widget")
        .appendQueryParameter("source", "native")
        .appendQueryParameter("platform", "android")
        .apply { locale?.let { appendQueryParameter("locale", it) } }
        .build().toString()
}
