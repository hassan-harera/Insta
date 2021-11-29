plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("kotlinx-serialization")
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

        testInstrumentationRunner = "com.harera.insta.KoinTestRunner"
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
    implementation(project(Core.common))
    implementation(project(Core.repository))
    implementation(project(Core.local))
    implementation(project(Core.remote))
    implementation(project(Core.components))

    implementation(project(Navigation.chatNav))
    implementation(project(Navigation.homeNav))

    implementation(project(Features.login))
    implementation(project(Features.notifications))
    implementation(project(Features.editProfile))
    implementation(project(Features.profile))
    implementation(project(Features.visitProfile))
    implementation(project(Features.post))
    implementation(project(Features.imagePosting))
    implementation(project(Features.textPosting))
    implementation(project(Features.feed))
    implementation(project(Features.search))

    implementation(project(UiComponents.post))

    implementation(Room.roomRuntime)
    kapt(Room.roomCompiler)

    implementation(Libs.appcompat)
    implementation(Libs.playServicesTasks)

    implementation(Compose.composeMaterial)
    implementation(Compose.composeRefresh)
    implementation(Compose.composUiTooling)
    implementation(Compose.composeDialog)

    /* Testing */
    implementation(Libs.truth)

    implementation(SocialLogin.facebookLogin)

    implementation(Libs.accompanistPager)
    implementation(Libs.coilCompose)
    implementation(Libs.serializer)
    implementation(Libs.testRunner)

    //ktor
    implementation(Ktor.ktorClient)
    implementation(Ktor.ktorClientAndroid)
    implementation(Ktor.ktorClientSerialization)
    implementation(Ktor.ktorClientGson)
    implementation(Ktor.ktorAuthClientGson)
    implementation(Ktor.ktorCio)

    //Koin
    implementation(DI.koinAndroid)
    implementation(DI.koinCore)
    implementation(DI.koinCompose)

    testImplementation(DI.koinTest)
    testImplementation(DI.koinJUnit4)

    androidTestImplementation(DI.koinTest)
    androidTestImplementation(DI.koinJUnit4)

    androidTestImplementation(Libs.coroutinesAndroid)
    androidTestImplementation(Libs.coroutinesTest)
}










