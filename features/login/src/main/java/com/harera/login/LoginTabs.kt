package com.harera.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting

typealias ComposeFun = @Composable () -> Unit

sealed class LoginTabs(var route: String, val title: String, val content: ComposeFun) {

    @OptIn(ExperimentalMaterialApi::class)
    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    object Login :
        LoginTabs(
            HomeNavigationRouting.HomeNotifications,
            "Login",
            { LoginScreen() }
        )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @OptIn(ExperimentalComposeUiApi::class)
    @ExperimentalCoilApi
    object Signup : LoginTabs(
        HomeNavigationRouting.HomeChats,
        "Signup",
        { SignupScreen() }
    )
}