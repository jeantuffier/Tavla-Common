import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

val kotlinVersion: String by extra
val kotlinxCoroutinesVersion: String by extra
val kotlinxDatetimeVersion: String by extra
val kotlinxSerializationVersion: String by extra
val ktorVersion: String by extra
val sqlDelightVersion: String by extra
val kodeinDiVersion: String by extra
val stateMachineVersion: String by extra
val kspVersion: String by extra
val arrowVersion: String by extra
val turbineVersion: String by extra

group = "no.entur.tavla"
version = "0.1.0"

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
    id("co.touchlab.faktory.kmmbridge")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap/")
        name = "ktorEap"
    }
    google()
    maven {
        url = uri("https://maven.pkg.github.com/jeantuffier/statemachine")
        name = "github"
        credentials(PasswordCredentials::class)
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.example")
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

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    ios {
        binaries.framework {
            baseName = "CommonTavla"
        }
    }
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-resources:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("com.jeantuffier.statemachine:core:$stateMachineVersion")
                implementation("com.jeantuffier.statemachine:orchestrate:$stateMachineVersion")
                implementation("io.arrow-kt:arrow-core:$arrowVersion")
                implementation("org.kodein.di:kodein-di:$kodeinDiVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
                implementation("app.cash.turbine:turbine:$turbineVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:sqlite-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
    }
}

android {
    namespace = "fr.jeantuffier"
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

tasks.named("compileKotlinJvm") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}
