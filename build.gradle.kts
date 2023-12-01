import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

val apolloVersion: String by extra
val koinVersion: String by extra
val kotlinVersion: String by extra
val kotlinxCoroutinesVersion: String by extra
val kotlinxDatetimeVersion: String by extra
val kotlinxSerializationVersion: String by extra
val kspVersion: String by extra
val ktorVersion: String by extra
val mockativeVersion: String by extra
val stateMachineVersion: String by extra
val arrowVersion: String by extra
val turbineVersion: String by extra

val GROUP: String by extra
val LIBRARY_VERSION: String by extra

group = GROUP
version = LIBRARY_VERSION

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.apollographql.apollo3")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
    id("maven-publish")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

allprojects {
    repositories {
        mavenCentral()
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/jeantuffier/Tavla-Common")
            name = "github"
            credentials {
                username = System.getenv("ACTIONS_USERNAME")
                password = System.getenv("ACTIONS_TOKEN")
            }
        }
    }
}

apollo {
    service("journey_planner") {
        packageName.set(GROUP)
    }
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

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
            export("com.jeantuffier.statemachine:core:$stateMachineVersion")
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
                implementation("com.apollographql.apollo3:apollo-runtime:$apolloVersion")
                implementation("io.arrow-kt:arrow-core:$arrowVersion")
                implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
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

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("app.cash.turbine:turbine:$turbineVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
            implementation("io.ktor:ktor-client-mock:$ktorVersion")
        }

        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:$ktorVersion")
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-ios:$ktorVersion")
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
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
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.withType<KotlinNativeCompile>().configureEach {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("compileKotlinJvm") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("jvmSourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("sourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}
