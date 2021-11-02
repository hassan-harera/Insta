apply {
    from("$rootDir/component-library-build.gradle")
}
dependencies {
    "implementation"(Libs.serializer)
    "implementation"(Room.roomRuntime)
}