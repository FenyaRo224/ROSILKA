plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("kapt")
}

android {
    namespace = "com.example.a1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.a1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Базовые зависимости
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.richeditor)
    implementation(libs.navigation.fragment)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Supabase зависимости
    implementation(platform("io.github.jan-tennert.supabase:bom:1.3.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    // Ktor
    implementation("io.ktor:ktor-client-android:2.3.3")
    implementation("io.ktor:ktor-client-core:2.3.3")
    implementation("io.ktor:ktor-client-cio:2.3.3")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // Корутины и сериализация
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Навигация
    implementation(libs.navigation.ui)

    // Core KTX
    implementation("androidx.core:core-ktx:1.12.0")

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}