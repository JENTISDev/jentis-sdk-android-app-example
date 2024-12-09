pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            credentials { username = "jp_gcs6hkvne2f97jdnosqm6mgnm3" }
        }
    }
}

rootProject.name = "JentisAppExample"
include(":app")