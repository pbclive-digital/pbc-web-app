rootProject.name = "PBCWebApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":lib-parent")
include(":lib-common-ui")
include(":lib-data")
include(":lib-network")
include(":lib-local-datastore")
include(":ui-splash")
include(":ui-dashboard")
include(":ui-event")
include(":ui-news")
include(":ui-auth")
include(":ui-question")
include(":ui-appointment")
include(":lib-pbc-container")
include(":ui-admin")
include(":lib-local-events")
