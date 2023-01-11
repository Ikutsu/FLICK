import de.fayard.refreshVersions.core.versionFor

plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.ikuzMirel.flick"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ikuzMirel.flick"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Android
    implementation (AndroidX.core.ktx)
    implementation (AndroidX.lifecycle.runtime.ktx)
    implementation (AndroidX.activity.compose)

    // Compose
    implementation (AndroidX.compose.ui)
    implementation (AndroidX.compose.ui.tooling)
    implementation (AndroidX.compose.ui.toolingPreview)
    implementation (AndroidX.compose.foundation)
    implementation (AndroidX.compose.runtime)
    implementation (AndroidX.compose.animation)
    implementation (AndroidX.compose.material3)
    implementation (AndroidX.compose.material.icons.extended)

    implementation (Google.android.playServices.maps)
    implementation (Google.android.maps.compose)

    implementation (COIL.compose)

    implementation (Google.accompanist.systemUiController)
    implementation (Google.accompanist.pager)

    testImplementation (Testing.junit4)
    androidTestImplementation (AndroidX.test.ext.junit)
    androidTestImplementation (AndroidX.test.espresso.core)
    androidTestImplementation (AndroidX.compose.ui.testJunit4)
    debugImplementation (AndroidX.compose.ui.tooling)
    debugImplementation (AndroidX.compose.ui.testManifest)
}