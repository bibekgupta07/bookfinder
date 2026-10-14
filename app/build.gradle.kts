import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    id("com.google.devtools.ksp") // Apply KSP plugin
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") // Apply Hilt plugin
}


    android {
        namespace = "com.bibekgupta.bookfinder"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.bibekgupta.bookfinder"
            minSdk = 28
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            // Google Books API key is read from local.properties (not committed)
            val localProperties = Properties().apply {
                val file = rootProject.file("local.properties")
                if (file.exists()) file.inputStream().use { load(it) }
            }
            buildConfigField(
                "String",
                "GOOGLE_BOOKS_API_KEY",
                "\"${localProperties.getProperty("GOOGLE_BOOKS_API_KEY", "")}\""
            )
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }
        kotlinOptions {
            jvmTarget = "18"
        }
        buildFeatures {
            compose = true
            buildConfig = true
        }
    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)


        // Retrofit & Network
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)
        implementation(libs.okhttp)

        // Jetpack Compose
        implementation("androidx.activity:activity-compose:1.8.2")
        implementation("androidx.compose.ui:ui:1.6.1")
        implementation("androidx.compose.material:material:1.6.1")
        implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
        implementation("androidx.compose.runtime:runtime-livedata:1.6.1") // For LiveData integration
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // For lifecycle-aware components

        // Jetpack Compose Navigation
        implementation("androidx.navigation:navigation-compose:2.7.6")

        // Material 3
        implementation("androidx.compose.material3:material3:1.2.0")
        implementation("androidx.compose.material:material-icons-extended:1.6.0")

        // Accompanist (Compose utilities)
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0") // For system UI control
        implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0") // For animated navigation

        // Coil (for image loading)
        implementation("io.coil-kt:coil-compose:2.5.0")

        // ViewModel and LiveData (for state management)
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

        // Hilt for Dependency Injection
        implementation("com.google.dagger:hilt-android:2.51.1")
        kapt("com.google.dagger:hilt-android-compiler:2.51.1")
        implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

        // Room components
        implementation("androidx.room:room-runtime:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")

        // DataStore Preferences
        implementation("androidx.datastore:datastore-preferences:1.0.0")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")



}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}