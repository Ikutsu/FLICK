pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// Refresh Versions
// https://jmfayard.github.io/refreshVersions/
plugins {
    id("de.fayard.refreshVersions") version "0.51.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "FLICK"
include (":app")
