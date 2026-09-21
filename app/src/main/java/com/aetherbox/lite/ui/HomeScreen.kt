package com.aetherbox.lite.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.aetherbox.lite.ui.theme.Ember
import com.aetherbox.lite.ui.theme.Fog
import com.aetherbox.lite.ui.theme.Foam
import com.aetherbox.lite.ui.theme.Ink
import com.aetherbox.lite.ui.theme.Lagoon
import com.aetherbox.lite.ui.theme.Mist
import com.aetherbox.lite.ui.theme.Seafoam
import kotlinx.coroutines.delay

private const val PREFS = "aetherbox_lite"
private const val PREF_SEEN_LIMITS = "seen_limits_modal"

private const val TERMUX_PKG = "com.termux"
private const val TERMUX_X11_PKG = "com.termux.x11"
private const val SHIZUKU_PKG = "moe.shizuku.privileged.api"
private const val AETHERBOX_PKG = "com.aetherbox.app"

private const val URL_TERMUX =
    "https://github.com/termux/termux-app/releases/latest"
private const val URL_TERMUX_X11 =
    "https://github.com/termux/termux-x11/releases/tag/nightly"
private const val URL_SHIZUKU =
    "https://shizuku.rikka.app/download/"
private const val URL_OMARCHY =
    "https://github.com/BlackFireAlex/omarchy-android"
private const val URL_OMARCHY_BUNDLE =
    "https://github.com/BlackFireAlex/omarchy-android/releases/download/v0.1.1/omarchy-android-aarch64-0.1.1.bundle.tar"
private const val URL_AETHERBOX =
    "https://github.com/AidansQwert/AetherBox/releases/latest"
private const val URL_AETHERBOX_OMARCHY_ROOTFS =
    "https://github.com/AidansQwert/AetherBox/releases/tag/omarchy-android-aarch64-0.1.1"
private const val URL_LITE_REPO =
    "https://github.com/AidansQwert/AetherBox-Lite"

private val OMARCHY_INSTALL = """
pkg update -y
pkg install -y git
git clone https://github.com/BlackFireAlex/omarchy-android.git
cd omarchy-android
./install.sh doctor
./install.sh --yes
""".trimIndent()

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }
    val scroll = rememberScrollState()
    var entered by remember { mutableStateOf(false) }
    var showLimitsModal by remember {
        mutableStateOf(!prefs.getBoolean(PREF_SEEN_LIMITS, false))
    }

    LaunchedEffect(Unit) {
        delay(40)
        entered = true
    }

    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.92f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    if (showLimitsModal) {
        LimitsModal(
            shizukuInstalled = isInstalled(context, SHIZUKU_PKG),
            onGetShizuku = { openUrl(context, URL_SHIZUKU) },
            onContinue = {
                prefs.edit().putBoolean(PREF_SEEN_LIMITS, true).apply()
                showLimitsModal = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Mist, Fog.copy(alpha = 0.55f), Mist)
                )
            )
    ) {
        AtmosphericGlow(pulse)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scroll)
                .padding(horizontal = 22.dp)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = entered,
                enter = fadeIn(tween(700)) + slideInVertically(
                    animationSpec = tween(700),
                    initialOffsetY = { it / 12 }
                )
            ) {
                Hero(
                    onStartOmarchy = { openUrl(context, URL_OMARCHY) },
                    onOpenTermux = {
                        if (!launchPackage(context, TERMUX_PKG)) {
                            openUrl(context, URL_TERMUX)
                        }
                    },
                    onShowLimits = { showLimitsModal = true }
                )
            }

            Spacer(Modifier.height(36.dp))
            SectionTitle("Setup without root")
            Spacer(Modifier.height(8.dp))
            Text(
                "AetherBox full needs Magisk / KernelSU. Lite walks you through Termux + PRoot (and optional Shizuku). Same Hyprland/Omarchy guest idea, different host — not native namespaces.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(22.dp))
            SetupStep(
                index = "01",
                title = "Termux",
                body = "Install from GitHub or F-Droid — not Play Store.",
                primary = if (isInstalled(context, TERMUX_PKG)) "Open Termux" else "Get Termux",
                onPrimary = {
                    if (!launchPackage(context, TERMUX_PKG)) openUrl(context, URL_TERMUX)
                }
            )
            SetupStep(
                index = "02",
                title = "Termux:X11",
                body = "Nightly build for the display server Omarchy expects.",
                primary = if (isInstalled(context, TERMUX_X11_PKG)) "Open X11" else "Get Termux:X11",
                onPrimary = {
                    if (!launchPackage(context, TERMUX_X11_PKG)) openUrl(context, URL_TERMUX_X11)
                }
            )
            SetupStep(
                index = "03",
                title = "Shizuku (optional)",
                body = "ADB-level helpers only. Does not unlock native AetherBox containers.",
                primary = if (isInstalled(context, SHIZUKU_PKG)) "Open Shizuku" else "Get Shizuku",
                secondary = "Why optional?",
                onPrimary = {
                    if (!launchPackage(context, SHIZUKU_PKG)) openUrl(context, URL_SHIZUKU)
                },
                onSecondary = { showLimitsModal = true }
            )
            SetupStep(
                index = "04",
                title = "Child process limit",
                body = "Settings → Developer options → Disable child process restrictions.",
                primary = "Open developer settings",
                onPrimary = { openDeveloperSettings(context) }
            )
            SetupStep(
                index = "05",
                title = "Omarchy Android",
                body = "Paste the installer in Termux. Needs ~8 GB free; guest uses ~4 GB.",
                primary = "Copy install commands",
                secondary = "Bundle download",
                onPrimary = {
                    copyText(context, OMARCHY_INSTALL, "Install commands copied")
                },
                onSecondary = { openUrl(context, URL_OMARCHY_BUNDLE) }
            )

            Spacer(Modifier.height(28.dp))
            SectionTitle("Also available")
            Spacer(Modifier.height(10.dp))
            Text(
                "Linux guest images (Omarchy, XFCE, …) live in the main AetherBox repo releases and rootfs feeds. Use the full app if you have root.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { openUrl(context, URL_AETHERBOX_OMARCHY_ROOTFS) },
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Lagoon, Seafoam))
                    )
                ) {
                    Text("Omarchy rootfs", color = Lagoon)
                }
                OutlinedButton(
                    onClick = {
                        if (!launchPackage(context, AETHERBOX_PKG)) {
                            openUrl(context, URL_AETHERBOX)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (isInstalled(context, AETHERBOX_PKG)) "Open AetherBox" else "Get AetherBox",
                        color = Lagoon
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
            TextButton(
                onClick = { openUrl(context, URL_LITE_REPO) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("AetherBox-Lite on GitHub", color = Lagoon)
            }
            Text(
                "Unofficial companion · Omarchy upstream stays with BlackFireAlex",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LimitsModal(
    shizukuInstalled: Boolean,
    onGetShizuku: () -> Unit,
    onContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onContinue,
        properties = DialogProperties(dismissOnClickOutside = false),
        shape = RoundedCornerShape(22.dp),
        containerColor = Foam,
        title = {
            Text(
                "Not native AetherBox",
                style = MaterialTheme.typography.titleLarge,
                color = Ink
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Lite is a no-root companion. It guides Termux + PRoot / Omarchy. It does not run the droidspaces binary or real Linux namespaces.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Do not expect native GPU, mounts, cgroups, or Magisk-level isolation. Hyprland and games will feel softer than rooted AetherBox.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Shizuku (optional) can help with some ADB-level host actions. It still will not make Lite equal to the rooted app.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lagoon,
                    contentColor = Foam
                )
            ) {
                Text("I understand")
            }
        },
        dismissButton = {
            TextButton(onClick = onGetShizuku) {
                Text(
                    if (shizukuInstalled) "Open Shizuku" else "Get Shizuku",
                    color = Ember
                )
            }
        }
    )
}

@Composable
private fun AtmosphericGlow(pulse: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.55f)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = 40.dp)
                .size(220.dp)
                .scale(pulse)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Seafoam.copy(alpha = 0.45f), Mist.copy(alpha = 0f)),
                        center = Offset(110f, 110f),
                        radius = 220f
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-40).dp)
                .size(260.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Lagoon.copy(alpha = 0.18f), Mist.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
    }
}

@Composable
private fun Hero(
    onStartOmarchy: () -> Unit,
    onOpenTermux: () -> Unit,
    onShowLimits: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
    ) {
        Text(
            text = "AetherBox Lite",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = "Linux desktop without root",
            style = MaterialTheme.typography.headlineMedium.copy(color = Lagoon)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Guided Termux + Omarchy (Hyprland) setup. Optional Shizuku. Not the native rooted runtime.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onStartOmarchy,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lagoon,
                    contentColor = Foam
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text("Omarchy guide")
            }
            TextButton(onClick = onOpenTermux) {
                Text("Open Termux", color = Ember)
            }
        }
        TextButton(onClick = onShowLimits) {
            Text("Read limits (Shizuku / native)", color = Lagoon)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun SetupStep(
    index: String,
    title: String,
    body: String,
    primary: String,
    onPrimary: () -> Unit,
    secondary: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Foam.copy(alpha = 0.72f))
            .border(1.dp, Lagoon.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = index,
                style = MaterialTheme.typography.labelLarge,
                color = Seafoam,
                modifier = Modifier.width(36.dp)
            )
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(8.dp))
        Text(text = body, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onPrimary,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lagoon,
                    contentColor = Foam
                )
            ) {
                Text(primary)
            }
            if (secondary != null && onSecondary != null) {
                TextButton(onClick = onSecondary) {
                    Text(secondary, color = Lagoon)
                }
            }
        }
    }
}

private fun isInstalled(context: Context, packageName: String): Boolean =
    try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }

private fun launchPackage(context: Context, packageName: String): Boolean {
    val launch = context.packageManager.getLaunchIntentForPackage(packageName) ?: return false
    launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(launch)
    return true
}

private fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

private fun openDeveloperSettings(context: Context) {
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

private fun copyText(context: Context, text: String, toast: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("aetherbox-lite", text))
    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
}
