import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildKonfig)
}

buildkonfig {
    packageName = "com.kavi.pbc.web.app"

    val environment = project.findProperty("app.env")?.toString() ?: "dev"

    // Default values
    defaultConfigs {
        when(environment) {
            "dev" -> { // Development variant
                buildConfigField(FieldSpec.Type.STRING, "API_SCHEME", "http")
                buildConfigField(FieldSpec.Type.STRING, "API_DOMAIN", "localhost:8082")
                buildConfigField(FieldSpec.Type.STRING, "ENVIRONMENT", "dev")
            }
            "staging" -> { // Staging variant
                buildConfigField(FieldSpec.Type.STRING, "API_SCHEME", "https")
                buildConfigField(FieldSpec.Type.STRING, "API_DOMAIN", "pbc-api-staging-1f3fe32cb947.herokuapp.com")
                buildConfigField(FieldSpec.Type.STRING, "ENVIRONMENT", "staging")
            }
            "prod" -> { // Production variant
                buildConfigField(FieldSpec.Type.STRING, "API_SCHEME", "https")
                buildConfigField(FieldSpec.Type.STRING, "API_DOMAIN", "pbc-api-staging-1f3fe32cb947.herokuapp.com")
                buildConfigField(FieldSpec.Type.STRING, "ENVIRONMENT", "staging")
            }
        }
    }
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

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Compose navigation
            implementation(libs.jetbrains.navigation.compose)

            implementation(projects.libParent)
            implementation(projects.libCommonUi)
            implementation(projects.libData)
            implementation(projects.libNetwork)
            implementation(projects.uiSplash)
            implementation(projects.uiAuth)
            implementation(projects.uiDashboard)
            implementation(projects.uiEvent)
            implementation(projects.uiNews)
        }
    }
}


