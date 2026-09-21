package com.aetherbox.lite.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.aetherbox.lite.data.LitePrefs
import com.aetherbox.lite.ui.components.CommandPreview
import com.aetherbox.lite.ui.components.SectionTitle
import com.aetherbox.lite.ui.components.SetupStep
import com.aetherbox.lite.ui.components.StatusCard
import com.aetherbox.lite.ui.theme.JetBrainsMono
import com.aetherbox.lite.ui.theme.SpaceGrotesk
import com.aetherbox.lite.util.SHIZUKU_PKG
import com.aetherbox.lite.util.TERMUX_PKG
import com.aetherbox.lite.util.TERMUX_X11_PKG
import com.aetherbox.lite.util.copyText
import com.aetherbox.lite.util.isInstalled
import com.aetherbox.lite.util.launchPackage
import com.aetherbox.lite.util.openDeveloperSettings
import com.aetherbox.lite.util.openUrl
import kotlinx.coroutines.delay

private const val URL_TERMUX = "https://github.com/termux/termux-app/releases/latest"
private const val URL_TERMUX_X11 = "https://github.com/termux/termux-x11/releases/tag/nightly"
private const val URL_SHIZUKU = "https://shizuku.rikka.app/download/"
private const val URL_PROOT_DISTRO = "https://github.com/termux/proot-distro"

private val UBUNTU_INSTALL = """
pkg update -y
pkg install -y proot-distro
proot-distro install ubuntu
proot-distro login ubuntu
""".trimIndent()

private val OMARCHY_INSTALL = """
pkg update -y
pkg install -y git
git clone https://github.com/BlackFireAlex/omarchy-android.git
cd omarchy-android
./install.sh doctor
./install.sh --yes
""".trimIndent()

@Composable
fun HomeTab(
    showLimits: Boolean,
    onShowLimits: () -> Unit,
    onDismissLimits: () -> Unit,
    onOpenDistros: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()
    val scheme = MaterialTheme.colorScheme
    var entered by remember { mutableStateOf(false) }

    val termux = isInstalled(context, TERMUX_PKG)
    val x11 = isInstalled(context, TERMUX_X11_PKG)
    val shizuku = isInstalled(context, SHIZUKU_PKG)

    LaunchedEffect(Unit) {
        delay(40)
        entered = true
    }

    if (showLimits) {
        LimitsModal(
            shizukuInstalled = shizuku,
            onGetShizuku = {
                if (!launchPackage(context, SHIZUKU_PKG)) openUrl(context, URL_SHIZUKU)
            },
            onContinue = {
                LitePrefs.setSeenLimits(context)
                onDismissLimits()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(bottom = 20.dp)
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = entered,
            enter = fadeIn(tween(500)) + slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = { it / 16 }
            )
        ) {
            Hero(
                onCopyUbuntu = {
                    copyText(context, UBUNTU_INSTALL, "Ubuntu commands copied")
                },
                onOpenTermux = {
                    if (!launchPackage(context, TERMUX_PKG)) openUrl(context, URL_TERMUX)
                },
                onBrowseDistros = onOpenDistros,
                onShowLimits = onShowLimits
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(8.dp))
            SectionTitle("Status")
            Spacer(Modifier.height(10.dp))
            StatusCard(
                title = "Termux",
                ready = termux,
                detail = if (termux) "Installed" else "Required for every Lite path",
                action = if (termux) "Open" else "Get",
                onAction = {
                    if (!launchPackage(context, TERMUX_PKG)) openUrl(context, URL_TERMUX)
                }
            )
            Spacer(Modifier.height(8.dp))
            StatusCard(
                title = "Termux:X11",
                ready = x11,
                detail = if (x11) "Installed" else "Needed for Omarchy desktop",
                action = if (x11) "Open" else "Get",
                onAction = {
                    if (!launchPackage(context, TERMUX_X11_PKG)) openUrl(context, URL_TERMUX_X11)
                }
            )
            Spacer(Modifier.height(8.dp))
            StatusCard(
                title = "Shizuku",
                ready = shizuku,
                detail = if (shizuku) "Optional helper" else "Optional — not native containers",
                action = if (shizuku) "Open" else "Get",
                onAction = {
                    if (!launchPackage(context, SHIZUKU_PKG)) openUrl(context, URL_SHIZUKU)
                }
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle("Shared setup")
            Spacer(Modifier.height(8.dp))
            Text(
                "Full AetherBox needs Magisk / KernelSU. Lite is Termux + PRoot — not native namespaces.",
                style = MaterialTheme.typography.bodyLarge,
                color = scheme.onSurface.copy(alpha = 0.85f)
            )
            SetupStep(
                index = "01",
                title = "Termux",
                body = "Install from GitHub or F-Droid — not Play Store.",
                primary = if (termux) "Open Termux" else "Get Termux",
                onPrimary = {
                    if (!launchPackage(context, TERMUX_PKG)) openUrl(context, URL_TERMUX)
                }
            )
            SetupStep(
                index = "02",
                title = "Child process limit",
                body = "Settings → Developer options → Disable child process restrictions.",
                primary = "Open developer settings",
                onPrimary = { openDeveloperSettings(context) }
            )

            Spacer(Modifier.height(20.dp))
            SectionTitle("Quick paths")
            Spacer(Modifier.height(8.dp))
            SetupStep(
                index = "A",
                title = "Ubuntu terminal",
                body = "CLI only. Best no-root start — copy and paste in Termux.",
                primary = "Copy install",
                secondary = "Docs",
                onPrimary = {
                    copyText(context, UBUNTU_INSTALL, "Ubuntu commands copied")
                },
                onSecondary = { openUrl(context, URL_PROOT_DISTRO) }
            )
            CommandPreview(UBUNTU_INSTALL)
            SetupStep(
                index = "B",
                title = "Omarchy desktop",
                body = "Hyprland via Termux:X11. Heavy (~8 GB free). Prefer Distros tab for more guests.",
                primary = "Copy install",
                secondary = "Browse all",
                onPrimary = {
                    copyText(context, OMARCHY_INSTALL, "Omarchy commands copied")
                },
                onSecondary = onOpenDistros
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
    val scheme = MaterialTheme.colorScheme
    AlertDialog(
        onDismissRequest = onContinue,
        properties = DialogProperties(dismissOnClickOutside = false),
        shape = RoundedCornerShape(24.dp),
        containerColor = scheme.surface,
        title = {
            Text("Not native AetherBox", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Lite guides Termux + PRoot. Distros tab lists CLI bases and desktop guests. Neither runs the droidspaces binary.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Do not expect native GPU, mounts, or Magisk-level isolation.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Shizuku is optional and still will not make Lite equal to the rooted app.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(onClick = onContinue, shape = RoundedCornerShape(16.dp)) {
                Text("I understand")
            }
        },
        dismissButton = {
            TextButton(onClick = onGetShizuku) {
                Text(if (shizukuInstalled) "Open Shizuku" else "Get Shizuku")
            }
        }
    )
}

@Composable
private fun Hero(
    onCopyUbuntu: () -> Unit,
    onOpenTermux: () -> Unit,
    onBrowseDistros: () -> Unit,
    onShowLimits: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            scheme.primary.copy(alpha = 0.26f),
                            scheme.tertiary.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 14.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "AETHERBOX",
                style = MaterialTheme.typography.labelMedium,
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold,
                color = scheme.primary,
                letterSpacing = 3.2.sp
            )
            Text(
                text = "AetherBox Lite",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.1).sp,
                    lineHeight = 40.sp
                ),
                color = scheme.onBackground
            )
            Text(
                text = "Spaces · catalog · Termux",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = scheme.onSurfaceVariant.copy(alpha = 0.88f)
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onCopyUbuntu,
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Ubuntu setup")
                }
                TextButton(onClick = onBrowseDistros) {
                    Text("Distros")
                }
                TextButton(onClick = onOpenTermux) {
                    Text("Termux")
                }
            }
            TextButton(onClick = onShowLimits) {
                Text("Read limits")
            }
        }
    }
}
