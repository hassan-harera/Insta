apply {
    from("$rootDir/feature-library-build.gradle")
}

dependencies {
    "implementation"(project(Core.repository))
    "implementation"(project(Core.components))

    "implementation"(Libs.hiltAndroid)
    "kapt"(Libs.hiltAndroidCompiler)
}