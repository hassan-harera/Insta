apply {
    from("$rootDir/feature-library-build.gradle")
}
dependencies {
    "implementation"(project(UiComponents.post))
    "implementation"(project(Core.repository))
}