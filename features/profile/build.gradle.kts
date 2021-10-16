apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "api"(Libs.lottieCompose)
    "api"(Compose.composeNavigation)
    "api"(Compose.composeActivity)
    "api"(Compose.composeAndroidX)
    "api"(Compose.composUiTooling)
    "api"(Compose.composeLivedata)
    "api"(Compose.composeMaterial)
    "api"(Compose.composePreview)
    "api"(Compose.composJUnitTest)
    "api"(Compose.composeDialog)
    "api"(Compose.composeData)
    "api"(Libs.coilCompose)
    "api"(Libs.accompanistPager)

    "implementation"(project(Core.base))
    "implementation"(project(Core.repository))
}