# Kept for future minify experiments. Release currently ships without R8 —
# proguard-android-optimize was removing Compose ComposableSingletons and
# crashing on launch (APK ~2MB). Do not re-enable optimize without verifying.

-keep class com.aetherbox.lite.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.runtime.internal.** { *; }
