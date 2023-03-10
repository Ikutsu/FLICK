import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import de.fayard.refreshVersions.core.versionFor

@Suppress("UnstableApiUsage")
plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
    kotlin ("kapt")
    id("com.google.dagger.hilt.android")
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
            buildConfigField("String", "ServerUrl", localProperties["server.home"].toString())
        }
        create("Work") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerUrl", localProperties["server.work"].toString())
        }
        create("Prod") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerUrl", localProperties["server.prod"].toString())
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
    implementation (Google.accompanist.pager)

    //Hilt dagger
    implementation (Google.dagger.hilt.android)
    implementation("androidx.core:core-ktx:1.9.0")
    kapt (Google.dagger.hilt.compiler)
    kapt (AndroidX.hilt.compiler)
    implementation (AndroidX.hilt.navigationCompose)

    //Retrofit
    implementation (Square.retrofit2.retrofit)
    implementation (Square.retrofit2.converter.moshi)

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