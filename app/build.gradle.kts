import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val kakaoAppKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""

val baseUrl = localProperties.getProperty("BASE_URL") ?: ""

android {
    namespace = "com.example.yourun"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.yourun"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoAppKey\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
    packaging.resources.excludes.add("META-INF/INDEX.LIST")
    packaging.resources.excludes.add("META-INF/DEPENDENCIES")
    packaging.resources.excludes.add("META-INF/NOTICE")
    packaging.resources.excludes.add("META-INF/LICENSE")
}

dependencies {

    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0-beta01")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.core:core:1.12.0")
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("com.kakao.sdk:v2-all:2.20.6")
    implementation ("com.kakao.maps.open:android:2.12.8")
    implementation ("com.google.android.gms:play-services-location:21.0.1") // FusedLocationProviderClient를 위한 의존성
    implementation("com.kakao.maps.open:android:2.12.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation(libs.firebase.appdistribution.gradle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
