import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    FileInputStream(keystorePropertiesFile).use {
        keystoreProperties.load(it)
    }
}

android {
    namespace = "com.aetherbox.lite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aetherbox.lite"
        minSdk = 26
        targetSdk = 34
        versionCode = 11
        versionName = "0.1.1"
        setProperty("archivesBaseName", "AetherBox-Lite-$versionName")
    }

    signingConfigs {
        val keystoreCandidates = listOf(
            file("droidspaces.keystore"),
            rootProject.file("droidspaces.keystore"),
            rootProject.file("../Droidspaces-OSS/droidspaces.keystore"),
            rootProject.file("../Droidspaces-OSS/ci-debug.keystore")
        )
        val keystoreFile = keystoreCandidates.firstOrNull { it.exists() }
        // Prefer local.properties / -P; fall back to CI debug keystore password.
        val keystorePassword = keystoreProperties["KEYSTORE_PASSWORD"] as String?
            ?: project.findProperty("KEYSTORE_PASSWORD") as String?
            ?: if (keystoreFile?.name?.contains("ci-debug") == true) "android" else ""
        val keyAliasName = keystoreProperties["KEY_ALIAS"] as String?
            ?: project.findProperty("KEY_ALIAS") as String?
            ?: if (keystoreFile?.name?.contains("ci-debug") == true) "droidspaces" else "droidspaces"
        val actualKeyPassword = keystoreProperties["KEY_PASSWORD"] as String?
            ?: project.findProperty("KEY_PASSWORD") as String?
            ?: keystorePassword

        if (keystoreFile != null && keystorePassword.isNotEmpty()) {
            create("release") {
                storeFile = keystoreFile
                storePassword = keystorePassword
                keyAlias = keyAliasName
                keyPassword = actualKeyPassword
            }
        }
    }

    buildTypes {
        release {
            // R8 optimize was stripping Compose runtime singletons and force-closing on launch.
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
