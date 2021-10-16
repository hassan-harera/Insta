plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}
android {
    compileSdk = DefaultConfig.compileSdk
    buildToolsVersion = DefaultConfig.buildToolsVersion

    defaultConfig {
        applicationId = DefaultConfig.appId
        minSdk = DefaultConfig.minSdk
        targetSdk = DefaultConfig.targetSdk
        versionCode = Releases.versionCode
        versionName = Releases.versionName

//        testInstrumentationRunner = "com.codingwithmitch.dotainfo.CustomTestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Libs.appcompat)

    implementation(project(Features.chat))
    implementation(project(Features.posts))
    implementation(project(Features.notifications))

    implementation(project(Core.repository))
    implementation(project(Core.base))
    implementation(project(Core.navigation))
    implementation(project(Core.components))

    implementation(Compose.composeMaterial)
    implementation(Libs.coilCompose)
    implementation(Libs.accompanistPager)
    implementation(Compose.composUiTooling)
    implementation(Compose.composeDialog)

    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltAndroidCompiler)
}










