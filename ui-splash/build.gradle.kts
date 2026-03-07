import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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

                // Compose dependencies
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)

                // Compose navigation
                implementation(libs.jetbrains.navigation.compose)
                // ViewModel lifecycle
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                // ktorfit
                implementation(libs.ktorfit.lib)

                implementation(projects.libParent)
                implementation(projects.libData)
                implementation(projects.libLocalDatastore)
                implementation(projects.libNetwork)
            }
        }
    }

}