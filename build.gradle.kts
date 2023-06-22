@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias (libs.plugins.android.application) apply false
    alias (libs.plugins.kotlin) apply false
    alias (libs.plugins.hilt) apply false
    alias (libs.plugins.mapsplatform.secrets.plugin) apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
