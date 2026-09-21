package com.aetherbox.lite.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.aetherbox.lite.data.LitePrefs
import com.aetherbox.lite.ui.theme.AetherBoxLiteTheme
import com.aetherbox.lite.ui.theme.LitePalette

private enum class LiteTab(val label: String) {
    HOME("Home"),
    DISTROS("Distros"),
    PANEL("Panel")
}

@Composable
fun LiteApp() {
    val context = LocalContext.current
    var dark by remember { mutableStateOf(LitePrefs.isDark(context)) }
    var palette by remember { mutableStateOf(LitePalette.fromName(LitePrefs.palette(context))) }
    var tab by remember { mutableStateOf(0) }
    var showLimits by remember { mutableStateOf(!LitePrefs.seenLimits(context)) }

    val activity = context as? ComponentActivity
    SideEffect {
        activity?.window?.let { window ->
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = !dark
        }
    }

    AetherBoxLiteTheme(darkTheme = dark, palette = palette) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    LiteTab.entries.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = { Text(item.label.take(1)) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (tab) {
                    0 -> HomeTab(
                        showLimits = showLimits,
                        onShowLimits = { showLimits = true },
                        onDismissLimits = { showLimits = false },
                        onOpenDistros = { tab = 1 }
                    )
                    1 -> DistrosTab()
                    else -> PanelTab(
                        darkTheme = dark,
                        onDarkChange = {
                            dark = it
                            LitePrefs.setDark(context, it)
                        },
                        palette = palette,
                        onPaletteChange = {
                            palette = it
                            LitePrefs.setPalette(context, it.name)
                        },
                        onShowLimits = { showLimits = true }
                    )
                }
            }
        }
    }
}
