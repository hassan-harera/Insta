package com.harera.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting

typealias ComposeFun = @Composable () -> Unit

sealed class LoginTabs(var route: String, val title: String, val content: ComposeFun) {

    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    object Login :
        LoginTabs(
            HomeNavigationRouting.HomeNotifications,
            "Login",
            { LoginScreen() }
        )

    @ExperimentalCoilApi
    @OptIn(ExperimentalComposeUiApi::class)
    object Signup : LoginTabs(
        HomeNavigationRouting.HomeChats,
        "Signup",
        { SignupScreen() }
    )
}