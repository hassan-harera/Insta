import Versions.compose_version

object Compose {
    const val composJUnitTest = "androidx.compose.ui:ui-test-junit4:${compose_version}"
    const val composUiTooling = "androidx.compose.ui:ui-tooling:$compose_version"
    const val composeAndroidX = "androidx.compose.ui:ui:$compose_version"
    const val composeDialog =
        "io.github.vanpra.compose-material-dialogs:datetime:${Versions.material_compose}"
    const val composeMaterial = "androidx.compose.material:material:$compose_version"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${compose_version}"
    const val composeData = "androidx.compose.ui:ui-tooling-data:${compose_version}"
    const val composeActivity = "androidx.activity:activity-compose:1.3.1"
    const val composeLivedata = "androidx.compose.runtime:runtime-livedata:1.1.0-alpha02"
    const val composeNavigation = "androidx.navigation:navigation-compose:2.4.0-alpha10"
}
