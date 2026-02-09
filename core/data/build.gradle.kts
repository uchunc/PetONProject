plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.woo.peton.core.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures{
        compose = true
    }
}
kotlin{
    jvmToolchain(21)
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.libs)

    implementation(libs.hilt.android)
    implementation(libs.firebase.firestore)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.play.services.location)

    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar)
}