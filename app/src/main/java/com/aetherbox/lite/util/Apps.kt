package com.aetherbox.lite.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast

const val TERMUX_PKG = "com.termux"
const val TERMUX_X11_PKG = "com.termux.x11"
const val SHIZUKU_PKG = "moe.shizuku.privileged.api"
const val AETHERBOX_PKG = "com.aetherbox.app"

fun isInstalled(context: Context, packageName: String): Boolean =
    try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }

fun launchPackage(context: Context, packageName: String): Boolean {
    val launch = context.packageManager.getLaunchIntentForPackage(packageName) ?: return false
    launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(launch)
    return true
}

fun openUrl(context: Context, url: String) {
    context.startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

fun openDeveloperSettings(context: Context) {
    try {
        context.startActivity(
            Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        context.startActivity(
            Intent(android.provider.Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

fun copyText(context: Context, text: String, toast: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("aetherbox-lite", text))
    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
}
