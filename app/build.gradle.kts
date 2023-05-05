import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import de.fayard.refreshVersions.core.versionFor

@Suppress("UnstableApiUsage")
plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    kotlin ("kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val localProperties = gradleLocalProperties(rootDir)

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
        debug {
            isDebuggable = true
        }
        create("Home") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerIP", localProperties["server.home"].toString())
        }
        create("Work") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerIP", localProperties["server.work"].toString())
        }
        create("Prod") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerIP", localProperties["server.prod"].toString())
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

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    // Android
    implementation (AndroidX.core.ktx)
    implementation (AndroidX.lifecycle.runtime.ktx)
    implementation (AndroidX.lifecycle.viewModelKtx)
    implementation (AndroidX.lifecycle.runtime.compose)
    implementation (AndroidX.activity.compose)
    implementation (AndroidX.core.splashscreen)

    // Compose
    implementation (AndroidX.compose.ui)
    implementation (AndroidX.compose.ui.tooling)
    implementation (AndroidX.compose.ui.toolingPreview)
    implementation (AndroidX.compose.foundation)
    implementation (AndroidX.compose.runtime)
    implementation (AndroidX.compose.animation)
    implementation (AndroidX.compose.material3)
    implementation (AndroidX.compose.material.icons.extended)

    //Google map
    implementation (Google.android.playServices.maps)
    implementation (Google.android.maps.compose)

    //Coil
    implementation (COIL.compose)

    //Accompanist
    implementation (Google.accompanist.systemUiController)

    //Hilt dagger
    implementation (Google.dagger.hilt.android)
    kapt (Google.dagger.hilt.compiler)
    kapt (AndroidX.hilt.compiler)
    implementation (AndroidX.hilt.navigationCompose)

    //Ktor
    implementation (Ktor.client.core)
    implementation (Ktor.client.cio)
    implementation (Ktor.plugins.websockets)
    implementation (Ktor.client.serialization)
    implementation (Ktor.client.logging)
    implementation (Ktor.client.contentNegotiation)
    implementation (Ktor.plugins.serialization.kotlinx.json)

    //Room
    implementation (AndroidX.room.runtime)
    annotationProcessor (AndroidX.room.compiler)
    ksp (AndroidX.room.compiler)
    implementation (AndroidX.room.ktx)

    //Compose destinations
    implementation(libs.animations.core)
    ksp (libs.ksp)

    //DataStore
    implementation (AndroidX.dataStore.preferences)

    //Testing
    testImplementation (Testing.junit4)
    androidTestImplementation (AndroidX.test.ext.junit)
    androidTestImplementation (AndroidX.test.espresso.core)
    androidTestImplementation (AndroidX.compose.ui.testJunit4)
    debugImplementation (AndroidX.compose.ui.tooling)
    debugImplementation (AndroidX.compose.ui.testManifest)
}