apply {
    from("$rootDir/android-library-build.gradle")
}


dependencies {
    "implementation"(Libs.firebaseFirestore)
    "implementation"(Libs.playServicesAuth)
    "implementation"(Libs.appcompat)
}