import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias (libs.plugins.android.application)
    alias (libs.plugins.kotlin)
    alias (libs.plugins.mapsplatform.secrets.plugin)
    alias (libs.plugins.ksp)
    kotlin ("kapt")
    alias (libs.plugins.hilt)
    alias (libs.plugins.serialization)
}

val localProperties = gradleLocalProperties(rootDir)

android {
    namespace = "com.ikuzMirel.flick"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ikuzMirel.flick"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = false

            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        debug {
            isDebuggable = true
        }
        create("dev") {
            initWith(getByName("debug"))
            buildConfigField("String", "ServerIP", localProperties["ServerIP"].toString())
        }

        create("prod") {
            initWith(getByName("release"))
            buildConfigField("String", "ServerIP", localProperties["ServerIP"].toString())
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    // Android
    implementation (libs.core)
    implementation (libs.lifecycle.runtime)
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.lifecycle.runtime.compose)
    implementation (libs.activity)
    implementation (libs.splashscreen)
    implementation (libs.workManager)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation (libs.ui)
    implementation (libs.ui.tooling)
    implementation (libs.ui.tooling.preview)
    implementation (libs.foundation)
    implementation (libs.compose.runtime)
    implementation (libs.animation)
    implementation (libs.material3)
    implementation (libs.material.icons)

    //Google map
//    implementation (libs.maps)
//    implementation (libs.maps.compose)

    //Coil
    implementation (libs.coil)

    //Accompanist
    implementation (libs.accompanist.systemuicontroller)

    //Hilt dagger
    implementation (libs.dagger.hilt)
    kapt (libs.dagger.hilt.compiler)
    kapt (libs.hilt.compiler)
    implementation (libs.hilt.navigation.compose)
    implementation (libs.hilt.work)

    //Ktor
    implementation (libs.ktor.core)
    implementation (libs.ktor.cio)
    implementation (libs.ktor.websockets)
    implementation (libs.ktor.serialization)
    implementation (libs.ktor.logging)
    implementation (libs.ktor.contentNegotiation)
    implementation (libs.ktor.serialization.json)
    implementation (libs.ktor.auth)

    //Room
    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
    ksp (libs.room.compiler)
    implementation (libs.room)

    //Compose destinations
    implementation(libs.destinations.core)
    ksp (libs.destinations.ksp)

    //DataStore
    implementation (libs.datastore)

    //Bson
    implementation (libs.bson)

    //Testing
    testImplementation (libs.junit)
    androidTestImplementation (libs.junit.ext)
    androidTestImplementation (libs.espresso)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation (libs.ui.testJunit4)
    debugImplementation (libs.ui.tooling)
    debugImplementation (libs.ui.testManifest)
}