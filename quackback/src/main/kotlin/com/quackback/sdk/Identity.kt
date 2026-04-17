package com.quackback.sdk

/**
 * Identity to pass to [Quackback.configure] so the widget can associate activity
 * with the current user at setup time.
 *
 * Equivalent to calling [Quackback.identify] immediately after configure.
 */
sealed class Identity {
    /**
     * Start an anonymous session. The widget prompts for an email inline the
     * first time the user posts feedback.
     */
    object Anonymous : Identity()

    /**
     * Identify the current user by their details. Simplest option — works out
     * of the box. Turn on "Verified identity only" in Admin → Settings → Widget
     * to require [SsoToken] instead.
     */
    data class User(
        val id: String,
        val email: String,
        val name: String? = null,
        val avatarURL: String? = null,
    ) : Identity()

    /** Identify the current user with a server-signed JWT. Blocks impersonation. */
    data class SsoToken(val token: String) : Identity()
}
