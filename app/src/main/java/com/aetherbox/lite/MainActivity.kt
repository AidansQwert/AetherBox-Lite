package com.aetherbox.lite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.aetherbox.lite.ui.LiteApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = true
        } catch (_: Exception) {
            // Older OEMs sometimes choke on edge-to-edge; UI still works.
        }
        setContent {
            LiteApp()
        }
    }
}
