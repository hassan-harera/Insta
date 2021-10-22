plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
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

        testInstrumentationRunner = "com.harera.repository.KoinTestRunner"
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
    implementation(project(Core.repository))
    implementation(project(Core.base))
    implementation(project(Core.components))

    implementation(project(Features.chat))
    implementation(project(Features.login))
    implementation(project(Features.notifications))
    implementation(project(Features.editProfile))
    implementation(project(Features.profile))
    implementation(project(Features.visitProfile))
    implementation(project(Features.post))
    implementation(project(Features.posting))
    implementation(project(Features.feed))

    implementation(project(UiComponents.post))

    implementation(Libs.appcompat)
    implementation(Libs.playServicesTasks)

    implementation(Compose.composeMaterial)
    implementation(Libs.coilCompose)
    implementation(Libs.accompanistPager)
    implementation(Compose.composUiTooling)
    implementation(Compose.composeDialog)


    /* Testing */
    implementation(Libs.playServicesAuth)

    //Koin
    implementation(DI.koinAndroid)
    implementation(DI.koinCore)
    implementation(DI.koinCompose)

    testImplementation(DI.koinTest)
    testImplementation(DI.koinJUnit4)

    androidTestImplementation(DI.koinTest)
    androidTestImplementation(DI.koinJUnit4)

    implementation(Libs.testRunner)
}










