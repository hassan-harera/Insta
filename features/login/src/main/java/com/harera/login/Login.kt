//package com.harera.login
//
//import android.content.Context
//import android.content.Intent
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.core.content.ContextCompat.startActivity
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.harera.base.navigation.home.HomeNavigation
//import com.harera.insta.HomeActivity
//
//@Preview
//@Composable
//fun LoginViewPreview() {
//}
//
//@Composable
//fun LoginView(
//    loginViewModel: LoginViewModel = hiltViewModel(),
//    navController: NavHostController,
//) {
//    val password = loginViewModel.password.observeAsState()
//    val email = loginViewModel.email.observeAsState()
//    val loading = loginViewModel.loading.observeAsState()
//    val exception = loginViewModel.exception.observeAsState()
//    val loginSuccess = loginViewModel.loginSuccess.observeAsState()
//
//    if (loginSuccess.value!!) {
//        navController.navigate(HomeNavigation.HomeFeed) {
//            launchSingleTop = true
//            restoreState = true
//        }
//    }
//
//    loginViewModel.formValidity.observe(this) {
//        bind.login.isEnabled = it.isValid
//
//        if (it.emailError != null) {
//            bind.email.error = getString(it.emailError!!)
//        } else if (it.passwordError != null) {
//            bind.password.error = getString(it.passwordError!!)
//        }
//    }
//}
//
//private fun goToHomeActivity() {
//}