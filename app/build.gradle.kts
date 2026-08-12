plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // Kotlin serialization plugin for type safe routes and navigation arguments
    // https://developer.android.com/guide/navigation#kts
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.location_client_android"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "com.example.location_client_android"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Fragments
    // https://developer.android.com/guide/fragments/create#kts
//    implementation("androidx.fragment:fragment-ktx:1.9.0")

    // Dependencies for app navigation
    // https://developer.android.com/guide/navigation#kts
    val nav_version = "2.9.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")                       // Jetpack Compose integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")                      // Views/Fragments integration
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")     // Feature module support for Fragments
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")            // Testing Navigation
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")                    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("com.google.android.gms:play-services-location:21.4.0")                      // Dependencies for location

    // Needed for depreciated libraries
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")                // Used in LocationServiceForeground.kt
    implementation("androidx.appcompat:appcompat:1.4.1")                                        // Used in LocationActivity.kt
    implementation("com.google.android.material:material:1.5.0")                                // Used in LocationActivity.kt

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}