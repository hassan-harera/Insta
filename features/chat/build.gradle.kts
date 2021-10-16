apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project (Core.base))
    "implementation"(project (Core.repository))
    "implementation"(project (Core.components))
    "implementation"(project (UiComponents.navigation))
    "implementation"(Compose.composeNavigation)
}
