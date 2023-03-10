plugins {
    id ("com.android.application") version "7.4.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("com.google.dagger.hilt.android") version "2.45" apply false
}

buildscript {
    val kotlin_version by extra("1.8.10")
    dependencies {
        classpath(libs.secrets.gradle.plugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
    repositories {
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}