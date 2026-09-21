# AetherBox Lite

Companion app for running a Linux desktop **without root**.

This is **not** AetherBox / Droidspaces. The full app needs Magisk or KernelSU and
runs real containers via the native `droidspaces` binary. Lite is a guided
Termux + PRoot / Omarchy (Hyprland) path. Expect softer GPU, namespaces, and
isolation than the rooted build.

## What stays where

| Piece | Repo |
| --- | --- |
| No-root companion APK (this app) | **AetherBox-Lite** (here) |
| Root AetherBox app + C runtime | [AetherBox](https://github.com/AidansQwert/AetherBox) |
| Linux guest rootfs catalogs / feeds | [AetherBox](https://github.com/AidansQwert/AetherBox) `Android/rootfs-feeds/` |

Omarchy and other guest images are published from the main AetherBox releases.
Lite only links and walks you through setup.

## Expectations

- No Magisk / KernelSU required
- Optional [Shizuku](https://shizuku.rikka.app/) can help with some elevated host
  actions, but it does **not** turn Lite into native AetherBox containers
- Guest is PRoot-style; do not expect root-level GPU, mount, or cgroup behaviour

## Build

```bash
./gradlew :app:assembleDebug
```

Release signing uses the same keystore properties as AetherBox when present in
`local.properties` (`KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`) and a
`droidspaces.keystore` next to this repo or paths you set yourself.

## License

Companion UI; Omarchy upstream remains with [BlackFireAlex/omarchy-android](https://github.com/BlackFireAlex/omarchy-android).
Rootfs packaging for rooted devices lives in AetherBox releases.
