import Versions.compose_version

object Versions {
    const val material_compose = "0.6.1"
    const val accompanist_version = "0.19.0"
    const val compose_version = "1.0.4"
    const val Ad_MOB_VERSION = "20.4.0"
    const val ACTIVITY_VERSION = "1.3.1"
    const val PLAY_SERVICE_TASKS = "17.2.1"
    const val LIFECYCLE_COMPILER_VERSION: String = "2.0.0"
    const val CONSTRAINT_LAYOUT_VERSION = "2.1.1"
    const val INTUIT_VERSION = "1.0.6"
    const val GLIGER_VERSION = "1.1.0"
    const val SEARCH_BAR = "0.7.5"
    const val CIRCLE_IMAGE = "3.1.0"
    const val ESPRESSO = "3.4"

    //Firebase
    const val FIREBASE_DATABASE_VERSION = "20.0.2"
    const val FIREBASE_AUTH_VERSION = "21.0.1"
    const val FIREBASE_FIRESTORE_VERSION = "23.0.3"
    const val FIREBASE_STORAGE_VERSION = "20.0.0"
    const val FIREBASE_BOM_VERSION = "26.1.1"

    //Google Services
    const val GOOGLE_AUTH_VERSION = "19.2.0"
    const val GOOGLE_AUTH_PHONE_VERSION = "17.5.1"
    const val GOOGLE_PLACES = "2.4.0"
    const val GOOGLE_MAPS_VERSION = "17.0.1"
    const val LEGACY_VERSION = "1.0.0"

    const val KOTLIN_VERSION = "1.5.31"
    const val HILT_VERSION = "2.37"
    const val NAV_VERSION = "2.3.1"
    const val COROUTINES_VERSION = "1.5.0"
    const val GRADLE_TOOLS_VERSION = "7.0.2"
    const val GMS_GOOGLE_SERVICES = "4.3.10"
    const val Jvm_TARGET = "4.3.10"

    const val FRAGMENT_VERSION = "1.3.3"
    const val LIFECYCLE_VERSION = "2.3.1"
    const val JETPACK_NAVIGATION_VERSION = "2.3.5"

    const val RETROFIT_VERSION = "2.9.0"
    const val LOGGING_INTERCEPTOR_VERSION = "4.9.0"
    const val MOCK_WEB_SERVER_VERSION = "4.9.0"

    const val COIL_VERSION = "1.3.2"

    const val TIMBER_VERSION = "4.7.1"

    const val PICASSO_VERSION = "2.71828"

    const val TEST_MOCKK_VERSION = "1.11.0"
    const val TEST_JUNIT_VERSION = "4.13.2"

    const val TEST_RUNNER_VERSION = "1.4.0"

    const val LEAK_CANARY_VERSION = "2.6"

    const val ANDROIDX_CORE_Testing_VERSION = "2.1.0"

    const val ANDROIDX_CORE_VERSION = "1.3.2"
    const val ANDROIDX_APP_COMPAT_VERSION = "1.3.1"
    const val GOOGLE_MATERIAL_VERSION = "1.4.0"
    const val ANDROIDX_SWIPE_REFRESH_LAYOUT_VERSION = "1.1.0"
    const val ANDROIDX_FRAGMENT_TESTING_VERSION = "1.3.3"

    const val DI_KOIN_VERSION = "3.0.2"
}


object Libs {
    //facebook login
//    const val facebookLogin = "com.facebook.android:facebook-android-sdk:${Versions.face}"

    const val coilCompose = "io.coil-kt:coil-compose:${Versions.COIL_VERSION}"
    const val accompanistPager =
        "com.google.accompanist:accompanist-pager:${Versions.accompanist_version}"

    const val lottieCompose = "com.airbnb.android:lottie-compose:4.0.0"

    //adMob
    const val adMob = "com.google.android.gms:play-services-ads-lite:${Versions.Ad_MOB_VERSION}"

    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_VERSION}"

    //Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT_VERSION}"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.HILT_VERSION}"
    const val hiltAndroidCompiler =
        "com.google.dagger:hilt-android-compiler:${Versions.HILT_VERSION}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HILT_VERSION}"
    const val hiltAnnotation = "androidx.hilt:hilt-compiler:${Versions.HILT_VERSION}"

    //UI
    const val activity = "androidx.activity:activity-ktx:${Versions.ACTIVITY_VERSION}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APP_COMPAT_VERSION}"
    const val materialDesign =
        "com.google.android.material:material:${Versions.GOOGLE_MATERIAL_VERSION}"

    //Image Loader
    const val picasso = "com.squareup.picasso:picasso:${Versions.PICASSO_VERSION}"

    //test
    const val testRunner = "androidx.test:runner:${Versions.TEST_RUNNER_VERSION}"
    const val androidCoreTesting =
        "androidx.arch.core:core-testing:${Versions.ANDROIDX_CORE_Testing_VERSION}"

    //gms play
    const val playServicesPlugin = "com.google.gms.google-services"
    const val playServicesMaps =
        "com.google.android.gms:play-services-maps:${Versions.GOOGLE_MAPS_VERSION}"

    const val googleServices = "com.google.gms:google-services:${Versions.GMS_GOOGLE_SERVICES}"
    const val playServicesTasks =
        "com.google.android.gms:play-services-tasks:${Versions.PLAY_SERVICE_TASKS}"
    const val playServicesAuth =
        "com.google.android.gms:play-services-auth:${Versions.GOOGLE_AUTH_VERSION}"

    const val playServicesAuthPhone =
        "com.google.android.gms:play-services-auth-api-phone:${Versions.GOOGLE_AUTH_PHONE_VERSION}"

    //firebase
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.FIREBASE_AUTH_VERSION}"

    const val firebaseDatabase =
        "com.google.firebase:firebase-database:${Versions.FIREBASE_DATABASE_VERSION}"

    const val firebaseStorage =
        "com.google.firebase:firebase-storage:${Versions.FIREBASE_STORAGE_VERSION}"

    const val firebaseFirestore =
        "com.google.firebase:firebase-firestore:${Versions.FIREBASE_FIRESTORE_VERSION}"

    const val firebaseBom =
        "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM_VERSION}"

    //Material SearchBar
    const val searchBar = "com.github.mancj:MaterialSearchBar:${Versions.SEARCH_BAR}"

    //dots indicator
    const val indicator = "com.tbuonomo:dotsindicator:4.2"

    const val gradleTools = "com.android.tools.build:gradle:${Versions.GRADLE_TOOLS_VERSION}"

    //circle image
    const val circleImage = "de.hdodenhof:circleimageview:${Versions.CIRCLE_IMAGE}"

    //gligar picker
    const val gligarPicker = "com.opensooq.supernova:gligar:${Versions.GLIGER_VERSION}"

    //intuit
    const val intuit = "com.intuit.sdp:sdp-android:${Versions.INTUIT_VERSION}"

    //places
    const val googlePlaces = "com.google.android.libraries.places:places:${Versions.GOOGLE_PLACES}"

    // Kotlin
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_VERSION}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN_VERSION}"

    // Coroutines
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_VERSION}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES_VERSION}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES_VERSION}"

    const val androidxCore = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE_VERSION}"

    // Fragments
    const val fragments = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_VERSION}"

    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_VERSION}"
    const val lifeCycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.LIFECYCLE_COMPILER_VERSION}"
    const val lifeCycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_VERSION}"
    const val viewModels =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_VERSION}"

    // SwipeRefresh layout
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.ANDROIDX_SWIPE_REFRESH_LAYOUT_VERSION}"

    //Navigation
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.JETPACK_NAVIGATION_VERSION}"

    const val navigationUi =
        "androidx.navigation:navigation-ui-ktx:${Versions.JETPACK_NAVIGATION_VERSION}"
    const val navigationSafeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.JETPACK_NAVIGATION_VERSION}"

    // ||
    // TESTING
    // ||

    // Junit 4
    const val junit4 = "junit:junit:${Versions.TEST_JUNIT_VERSION}"
    const val junitExt = "androidx.test.ext:junit:1.1.2"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"

    // Fragments
    const val fragmentTest =
        "androidx.fragment:fragment-testing:${Versions.ANDROIDX_FRAGMENT_TESTING_VERSION}"

}