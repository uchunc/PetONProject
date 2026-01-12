plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = "com.woo.peton"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.woo.peton"
        minSdk = 24
        targetSdk = 36
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}
kotlin{
    jvmToolchain(21)
}
secrets {
    // 사용할 프로퍼티 파일의 이름을 선언(선언하지 않으면: "local.properties")
    propertiesFileName = "secret.properties"

    // CI/CD 환경을 위한 기본 프로퍼티 파일을 지정
    // 이 파일은 버전 관리에 포함될 수 있음
    //defaultPropertiesFileName = "secrets.defaults.properties"

    // Secrets Plug-In 무시할 키의 목록을 정규 표현식으로 지정가능
    // "sdk.dir"은 기본적으로 무시
    //ignoreList.add("debug.*")           // "debug"로 시작하는 모든 키 무시
}
dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))

    implementation(project(":domain"))

    implementation(project(":features:home"))
    implementation(project(":features:auth"))
    implementation(project(":features:chatting"))
    implementation(project(":features:missingreport"))
    implementation(project(":features:mypage"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.libs)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)

    implementation(libs.hilt.android)
    implementation(libs.play.services.maps)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar)
}