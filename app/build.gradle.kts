plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    //id("kotlin-kapt")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.loeth.kindly"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.loeth.kindly"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
        compose = true
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
    implementation(libs.androidx.multidex)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (platform(libs.firebase.bom.v2900))
    implementation (libs.accompanist)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.firebase.storage.ktx)
    implementation (libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.splashscreen)
    ksp(libs.hilt.android.compiler)
    implementation(libs.compose.material)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.play.services.ads)

    testImplementation (libs.junit)
    testImplementation(libs.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.mockito.mockito.inline)
    testImplementation (libs.androidx.work.testing)
    testImplementation (libs.kotlinx.coroutines.test)

    debugImplementation(libs.ui.test.manifest)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation (libs.junit)



    implementation (libs.coil.compose)

    implementation (libs.kotlinx.coroutines.android)
}
