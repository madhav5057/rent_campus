plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.campus.rent"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.campus.rent"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //firebase
    implementation(libs.firebase.analytics)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    //location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.places)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    //lottie
    implementation(libs.lottie)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //glide and circular image view
    implementation(libs.glide)
    implementation(libs.circleimageview)


    //app write
    implementation("io.appwrite:sdk-for-android:6.1.0")

    //paging
    implementation(libs.androidx.paging.runtime)


}