pluginManagement {
    val kotlinVersion: String by settings
    val kmmBridgeVersion: String by settings
    val kspVersion: String by settings
    val nativeCoroutines: String by settings
    val sqlDelightVersion: String by settings
    plugins {
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("app.cash.sqldelight") version sqlDelightVersion apply false
        id("co.touchlab.faktory.kmmbridge") version kmmBridgeVersion apply false
        id("com.google.devtools.ksp") version kspVersion apply false
        id("com.rickclephas.kmp.nativecoroutines") version nativeCoroutines apply false
    }
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:7.4.2")
            }
        }
    }
}

rootProject.name = "common-tavla"
