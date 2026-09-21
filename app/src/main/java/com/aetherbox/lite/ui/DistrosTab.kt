package com.aetherbox.lite.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aetherbox.lite.data.DistroCatalog
import com.aetherbox.lite.data.DistroEntry
import com.aetherbox.lite.ui.components.CommandPreview
import com.aetherbox.lite.ui.components.SectionTitle
import com.aetherbox.lite.util.copyText
import com.aetherbox.lite.util.openUrl

@Composable
fun DistrosTab() {
    val context = LocalContext.current
    val scroll = rememberScrollState()
    var filter by remember { mutableStateOf(Filter.ALL) }
    var expandedId by remember { mutableStateOf<String?>(null) }

    val items = DistroCatalog.entries.filter {
        when (filter) {
            Filter.ALL -> true
            Filter.PROOT -> it.kind == DistroEntry.Kind.PROOT
            Filter.DESKTOP -> it.kind == DistroEntry.Kind.DESKTOP
            Filter.ROOTED -> it.kind == DistroEntry.Kind.ROOTFS_FEED
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        SectionTitle("Catalog")
        Spacer(Modifier.height(6.dp))
        Text(
            "Browse like rooted AetherBox — install happens in Termux (PRoot) or the full app.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Filter.entries.forEach { f ->
                FilterChip(
                    selected = filter == f,
                    onClick = { filter = f },
                    label = { Text(f.label) }
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        items.forEach { entry ->
            DistroCard(
                entry = entry,
                expanded = expandedId == entry.id,
                onToggle = {
                    expandedId = if (expandedId == entry.id) null else entry.id
                },
                onCopy = { cmds ->
                    copyText(context, cmds, "${entry.name} commands copied")
                },
                onOpen = { url -> openUrl(context, url) }
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

private enum class Filter(val label: String) {
    ALL("All"),
    PROOT("CLI"),
    DESKTOP("Desktop"),
    ROOTED("Rooted feeds")
}

@Composable
private fun DistroCard(
    entry: DistroEntry,
    expanded: Boolean,
    onToggle: () -> Unit,
    onCopy: (String) -> Unit,
    onOpen: (String) -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val kindLabel = when (entry.kind) {
        DistroEntry.Kind.PROOT -> "proot-distro"
        DistroEntry.Kind.DESKTOP -> "Termux desktop"
        DistroEntry.Kind.ROOTFS_FEED -> "AetherBox feed"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(18.dp),
        color = scheme.surface,
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(entry.name, style = MaterialTheme.typography.titleLarge)
                entry.sizeHint?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = scheme.primary
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                kindLabel.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = scheme.secondary
            )
            Spacer(Modifier.height(8.dp))
            Text(entry.tagline, style = MaterialTheme.typography.bodyMedium)

            if (expanded) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    entry.installCommands?.let { cmds ->
                        Button(
                            onClick = { onCopy(cmds) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Copy install")
                        }
                    }
                    entry.openUrl?.let { url ->
                        TextButton(onClick = { onOpen(url) }) {
                            Text(
                                if (entry.kind == DistroEntry.Kind.ROOTFS_FEED) "Open feed"
                                else "Open guide"
                            )
                        }
                    }
                }
                entry.installCommands?.let { CommandPreview(it) }
            } else {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tap for actions",
                    style = MaterialTheme.typography.bodySmall,
                    color = scheme.onSurfaceVariant
                )
            }
        }
    }
}
