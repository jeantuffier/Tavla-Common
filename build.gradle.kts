import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

val kotlinVersion: String by extra
val kotlinxCoroutinesVersion: String by extra
val kotlinxDatetimeVersion: String by extra
val kotlinxSerializationVersion: String by extra
val ktorVersion: String by extra
val sqlDelightVersion: String by extra
val koinVersion: String by extra
val stateMachineVersion: String by extra
val kspVersion: String by extra
val arrowVersion: String by extra
val turbineVersion: String by extra

val GROUP: String by extra
val LIBRARY_VERSION: String by extra

group = GROUP
version = LIBRARY_VERSION

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
    id("co.touchlab.faktory.kmmbridge")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
    id("maven-publish")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("no.entur.tavla")
        }
    }
}

addGithubPackagesRepository()
kmmbridge {
    mavenPublishArtifacts()
    githubReleaseVersions()
    spm()
    addGithubPackagesRepository()
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    jvm()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "CommonTavla"
            export("com.jeantuffier.statemachine:core:$stateMachineVersion\"")
            export("com.jeantuffier.statemachine:orchestrate:$stateMachineVersion")
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        val commonMain by getting {
            dependencies {
                api("com.jeantuffier.statemachine:core:$stateMachineVersion")
                api("com.jeantuffier.statemachine:orchestrate:$stateMachineVersion")
                implementation("io.arrow-kt:arrow-core:$arrowVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-resources:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("app.cash.turbine:turbine:$turbineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:sqlite-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
    }
}

android {
    namespace = "no.entur.tavla"
    compileSdk = 33
    defaultConfig {
        minSdk = 27
    }
}

dependencies {
    add("kspCommonMainMetadata", "com.jeantuffier.statemachine:processor:$stateMachineVersion")
}

tasks.named("build") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.withType<KotlinCompile>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.withType<KotlinNativeCompile>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.named("compileKotlinJvm") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}
