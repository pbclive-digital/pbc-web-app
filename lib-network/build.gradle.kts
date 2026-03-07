import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktorfit)
}

ktorfit {
    compilerPluginVersion = libs.versions.ktorfitCompilerVersion
}

kotlin {

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                // ktor dependencies
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.kotlinx.json)

                // kotlin coroutines
                implementation(libs.kotlinx.coroutines.core)

                // kotlin serialization
                implementation(libs.kotlin.serialization)

                // ktorfit
                implementation(libs.ktorfit.lib)

                implementation(projects.libData)
            }
        }
    }

}