package com.aetherbox.lite.ui

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aetherbox.lite.BuildConfig
import com.aetherbox.lite.ui.components.SectionTitle
import com.aetherbox.lite.ui.components.StatusCard
import com.aetherbox.lite.ui.theme.LitePalette
import com.aetherbox.lite.util.AETHERBOX_PKG
import com.aetherbox.lite.util.SHIZUKU_PKG
import com.aetherbox.lite.util.TERMUX_PKG
import com.aetherbox.lite.util.TERMUX_X11_PKG
import com.aetherbox.lite.util.isInstalled
import com.aetherbox.lite.util.launchPackage
import com.aetherbox.lite.util.openDeveloperSettings
import com.aetherbox.lite.util.openUrl

private const val URL_AETHERBOX = "https://github.com/AidansQwert/AetherBox/releases/latest"
private const val URL_LITE = "https://github.com/AidansQwert/AetherBox-Lite"
private const val URL_LITE_RELEASES = "https://github.com/AidansQwert/AetherBox-Lite/releases"
private const val URL_TERMUX = "https://github.com/termux/termux-app/releases/latest"
private const val URL_TERMUX_X11 = "https://github.com/termux/termux-x11/releases/tag/nightly"
private const val URL_SHIZUKU = "https://shizuku.rikka.app/download/"

@Composable
fun PanelTab(
    darkTheme: Boolean,
    onDarkChange: (Boolean) -> Unit,
    palette: LitePalette,
    onPaletteChange: (LitePalette) -> Unit,
    onShowLimits: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()
    val scheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        SectionTitle("Panel")
        Spacer(Modifier.height(6.dp))
        Text(
            "Companion health — not guest CPU/IP. Native metrics need rooted AetherBox.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(14.dp))
        StatusCard(
            title = "Termux",
            ready = isInstalled(context, TERMUX_PKG),
            detail = "Host shell",
            action = if (isInstalled(context, TERMUX_PKG)) "Open" else "Get",
            onAction = {
                if (!launchPackage(context, TERMUX_PKG)) openUrl(context, URL_TERMUX)
            }
        )
        Spacer(Modifier.height(8.dp))
        StatusCard(
            title = "Termux:X11",
            ready = isInstalled(context, TERMUX_X11_PKG),
            detail = "Display server",
            action = if (isInstalled(context, TERMUX_X11_PKG)) "Open" else "Get",
            onAction = {
                if (!launchPackage(context, TERMUX_X11_PKG)) openUrl(context, URL_TERMUX_X11)
            }
        )
        Spacer(Modifier.height(8.dp))
        StatusCard(
            title = "Shizuku",
            ready = isInstalled(context, SHIZUKU_PKG),
            detail = "Optional",
            action = if (isInstalled(context, SHIZUKU_PKG)) "Open" else "Get",
            onAction = {
                if (!launchPackage(context, SHIZUKU_PKG)) openUrl(context, URL_SHIZUKU)
            }
        )

        Spacer(Modifier.height(24.dp))
        SectionTitle("Appearance")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = scheme.surface,
            border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Dark theme", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Matches rooted AetherBox night mode",
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.onSurfaceVariant
                    )
                }
                Switch(checked = darkTheme, onCheckedChange = onDarkChange)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Palette", style = MaterialTheme.typography.titleSmall, color = scheme.primary)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            LitePalette.entries.forEach { p ->
                PaletteChip(
                    palette = p,
                    selected = palette == p,
                    dark = darkTheme,
                    onClick = { onPaletteChange(p) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            palette.displayName,
            style = MaterialTheme.typography.bodyMedium,
            color = scheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))
        SectionTitle("Device")
        Spacer(Modifier.height(10.dp))
        InfoRow("Android", "API ${Build.VERSION.SDK_INT} · ${Build.VERSION.RELEASE}")
        InfoRow("Device", "${Build.MANUFACTURER} ${Build.MODEL}")
        InfoRow("ABI", Build.SUPPORTED_ABIS.firstOrNull() ?: "?")
        InfoRow("Lite", "v${BuildConfig.VERSION_NAME}")

        Spacer(Modifier.height(24.dp))
        SectionTitle("Actions")
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = { openDeveloperSettings(context) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Developer settings")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onShowLimits,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Show limits again")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = { openUrl(context, URL_LITE_RELEASES) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Check Lite updates")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (!launchPackage(context, AETHERBOX_PKG)) openUrl(context, URL_AETHERBOX)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                if (isInstalled(context, AETHERBOX_PKG)) "Open rooted AetherBox"
                else "Get rooted AetherBox"
            )
        }
        TextButton(
            onClick = { openUrl(context, URL_LITE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AetherBox-Lite on GitHub")
        }
    }
}

@Composable
private fun PaletteChip(
    palette: LitePalette,
    selected: Boolean,
    dark: Boolean,
    onClick: () -> Unit
) {
    val color = if (dark) palette.primaryDark else palette.primaryLight
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color)
            .then(
                if (selected) Modifier.border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                else Modifier
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
