package com.aetherbox.lite.data

import android.content.Context

object LitePrefs {
    private const val PREFS = "aetherbox_lite"
    const val PREF_SEEN_LIMITS = "seen_limits_modal"
    private const val PREF_DARK = "dark_theme"
    private const val PREF_PALETTE = "palette"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun seenLimits(context: Context): Boolean =
        prefs(context).getBoolean(PREF_SEEN_LIMITS, false)

    fun setSeenLimits(context: Context, value: Boolean = true) {
        prefs(context).edit().putBoolean(PREF_SEEN_LIMITS, value).apply()
    }

    fun isDark(context: Context): Boolean =
        prefs(context).getBoolean(PREF_DARK, false)

    fun setDark(context: Context, dark: Boolean) {
        prefs(context).edit().putBoolean(PREF_DARK, dark).apply()
    }

    fun palette(context: Context): String =
        prefs(context).getString(PREF_PALETTE, "AETHER") ?: "AETHER"

    fun setPalette(context: Context, name: String) {
        prefs(context).edit().putString(PREF_PALETTE, name).apply()
    }
}
