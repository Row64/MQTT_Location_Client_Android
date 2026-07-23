

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mqtt_client_v4"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "com.example.mqtt_client_v4"
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
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources {
            excludes += listOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }


    buildFeatures {
        compose = true
    }

    /**
     * The following block was added to resolve a compile error after importing the
     * HiveMQ library.
     *
     * The error: "6 files found with path 'META-INF/INDEX.LIST' from inputs: ..."
     *
     * See the following article:
     *  https://community.hivemq.com/t/com-hivemq1-2-1/1259/2
     *
     * Had to change the quotation marks to get this to work.
     */
//    packagingOptions {
//        resources {
//            resources.excludes.add("META-INF/{AL2.0,LGPL2.1}")
//            resources.excludes.add("META-INF/LICENSE.md")
//            resources.excludes.add("META-INF/LICENSE-notice.md")
//            resources.excludes.add("META-INF/INDEX.LIST")
//            resources.excludes.add("META-INF/*.properties")
//        }
//    }

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
    implementation("com.hivemq:hivemq-mqtt-client:1.3.17")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}