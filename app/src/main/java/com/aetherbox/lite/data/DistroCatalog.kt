package com.aetherbox.lite.data

/** Curated distros Lite can guide into (PRoot / release links — not native spaces). */
data class DistroEntry(
    val id: String,
    val name: String,
    val tagline: String,
    val kind: Kind,
    val installCommands: String? = null,
    val openUrl: String? = null,
    val sizeHint: String? = null
) {
    enum class Kind { PROOT, DESKTOP, ROOTFS_FEED }
}

object DistroCatalog {
    val entries: List<DistroEntry> = listOf(
        DistroEntry(
            id = "ubuntu",
            name = "Ubuntu",
            tagline = "CLI base via proot-distro — smallest useful start.",
            kind = DistroEntry.Kind.PROOT,
            sizeHint = "~small",
            installCommands = """
pkg update -y
pkg install -y proot-distro
proot-distro install ubuntu
proot-distro login ubuntu
""".trimIndent()
        ),
        DistroEntry(
            id = "debian",
            name = "Debian",
            tagline = "Stable CLI base in Termux proot-distro.",
            kind = DistroEntry.Kind.PROOT,
            sizeHint = "~small",
            installCommands = """
pkg update -y
pkg install -y proot-distro
proot-distro install debian
proot-distro login debian
""".trimIndent()
        ),
        DistroEntry(
            id = "alpine",
            name = "Alpine",
            tagline = "Tiny musl rootfs — quick shell experiments.",
            kind = DistroEntry.Kind.PROOT,
            sizeHint = "~tiny",
            installCommands = """
pkg update -y
pkg install -y proot-distro
proot-distro install alpine
proot-distro login alpine
""".trimIndent()
        ),
        DistroEntry(
            id = "archlinux",
            name = "Arch Linux",
            tagline = "Rolling CLI base via proot-distro.",
            kind = DistroEntry.Kind.PROOT,
            sizeHint = "~medium",
            installCommands = """
pkg update -y
pkg install -y proot-distro
proot-distro install archlinux
proot-distro login archlinux
""".trimIndent()
        ),
        DistroEntry(
            id = "fedora",
            name = "Fedora",
            tagline = "Fedora rootfs in Termux proot-distro.",
            kind = DistroEntry.Kind.PROOT,
            sizeHint = "~medium",
            installCommands = """
pkg update -y
pkg install -y proot-distro
proot-distro install fedora
proot-distro login fedora
""".trimIndent()
        ),
        DistroEntry(
            id = "omarchy",
            name = "Omarchy (Hyprland)",
            tagline = "Desktop guest for Termux:X11. Heavy — ~4 GB installed.",
            kind = DistroEntry.Kind.DESKTOP,
            sizeHint = "~4 GB",
            openUrl = "https://github.com/BlackFireAlex/omarchy-android",
            installCommands = """
pkg update -y
pkg install -y git
git clone https://github.com/BlackFireAlex/omarchy-android.git
cd omarchy-android
./install.sh doctor
./install.sh --yes
""".trimIndent()
        ),
        DistroEntry(
            id = "omarchy-rootfs",
            name = "Omarchy rootfs (rooted)",
            tagline = "Same guest packaged for full AetherBox — needs Magisk.",
            kind = DistroEntry.Kind.ROOTFS_FEED,
            sizeHint = "~1.1 GB dl",
            openUrl = "https://github.com/AidansQwert/AetherBox/releases/tag/omarchy-android-aarch64-0.1.1"
        ),
        DistroEntry(
            id = "xfce-feed",
            name = "XFCE desktop feed",
            tagline = "AetherBox rootfs catalog entry — rooted install only.",
            kind = DistroEntry.Kind.ROOTFS_FEED,
            openUrl = "https://github.com/AidansQwert/AetherBox/tree/main/Android/rootfs-feeds"
        ),
        DistroEntry(
            id = "lxc-feed",
            name = "LXC community feed",
            tagline = "Community LXC images listed in AetherBox feeds.",
            kind = DistroEntry.Kind.ROOTFS_FEED,
            openUrl = "https://github.com/AidansQwert/AetherBox/blob/main/Android/rootfs-feeds/lxc-community.json"
        )
    )
}
