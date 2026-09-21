# AetherBox Lite Discontinued

Companion app for running Linux **without root**.

This is **not** AetherBox / Droidspaces. The full app needs Magisk or KernelSU and
runs real containers via the native `droidspaces` binary. Lite mirrors the rooted
app’s **Home / Distros / Panel** shape, but installs happen in Termux (PRoot) —
not native namespaces.

## What matches rooted AetherBox

| Root | Lite |
| --- | --- |
| Home + status | Status cards (Termux / X11 / Shizuku) |
| Rootfs catalog | Distros tab (CLI proot-distro + Omarchy + feed links) |
| Control panel | Panel (companion health, theme, device info) |
| Theme palettes | Dark mode + Aether / Nebula / Ocean / Graphite / Forest |
| Native spaces | **Not available** — needs Magisk |

## Paths

| Path | What you get |
| --- | --- |
| **CLI (proot-distro)** | Ubuntu, Debian, Alpine, Arch, Fedora — shell only |
| **Omarchy** | Hyprland desktop via Termux:X11 — heavy (~8 GB free) |
| **Rooted feeds** | Links into AetherBox rootfs releases (full app) |

## What stays where

| Piece | Repo |
| --- | --- |
| No-root companion APK (this app) | **AetherBox-Lite** (here) |
| Root AetherBox app + C runtime | [AetherBox](https://github.com/AidansQwert/AetherBox) |
| Linux guest rootfs catalogs / feeds | [AetherBox](https://github.com/AidansQwert/AetherBox) `Android/rootfs-feeds/` |

## Build

```bash
./gradlew :app:assembleDebug
```

## License

Companion UI; Omarchy upstream remains with [BlackFireAlex/omarchy-android](https://github.com/BlackFireAlex/omarchy-android).
Rootfs packaging for rooted devices lives in AetherBox releases.
