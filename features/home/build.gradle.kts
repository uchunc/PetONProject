plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.woo.peton.features.home"
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
    // Project Modules
    implementation(project(":domain"))      // 데이터 모델 & 레포지토리 인터페이스
    implementation(project(":core:ui"))     // 공통 UI 컴포넌트 (PetCard 등)
    implementation(project(":core:utils"))  // 날짜 변환 함수 (toFormattedString 등)

    // Android & Compose Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose BOM & Bundle
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.libs)

    // Image Loading (필수 추가)
    implementation(libs.coil.compose)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}