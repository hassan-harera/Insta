apply {
    from("$rootDir/android-library-build.gradle")
}


dependencies {
    "api"(project(Core.model))
    "implementation"(project(Core.base))
    "implementation"("com.facebook.android:facebook-login:12.0.1")
    "implementation"(Libs.playServicesAuth)
}