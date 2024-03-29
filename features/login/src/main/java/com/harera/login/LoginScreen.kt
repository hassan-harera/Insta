package com.harera.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.harera.base.state.LoginState
import com.harera.base.state.State
import com.harera.base.validity.LoginFormValidity
import com.harera.compose.ButtonToLogin
import com.harera.compose.FacebookRegisterButton
import com.harera.compose.GoogleRegisterButton
import com.harera.compose.Toast
import com.harera.home.HomeActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
@Preview(showBackground = true)
fun LoginScreen(
    loginViewModel: LoginViewModel = getViewModel(),
) {
    val email = loginViewModel.emailState
    val password = loginViewModel.passwordState
    var formValidity by remember { mutableStateOf(LoginFormValidity()) }

    val scope = rememberCoroutineScope()
    val state = loginViewModel.state
    val context = LocalContext.current

    when (state) {
        is LoginState.FormValidity -> {
            formValidity = state.formValidity
        }

        is LoginState.LoginSuccess -> {
            context.startActivity(Intent(context, HomeActivity::class.java))
            (context as Activity).finish()
        }

        is State.Error -> {
            Toast(state.data.toString())
        }

        is State.Loading -> {
            CircularProgressIndicator()
        }
    }

    Column(
        Modifier.padding(15.dp),
    ) {
        OutlinedTextField(
            value = email,
            isError = formValidity.emailError,
            onValueChange = {
                loginViewModel.setEmail(it)
            },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next),
        )

        Spacer(modifier = Modifier.height(20.dp))


        OutlinedTextField(
            value = password,
            isError = formValidity.passwordError,
            onValueChange = { loginViewModel.setPassword(it) },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonToLogin(isEnabled = formValidity.isValid) {
            scope.launch {
                loginViewModel.login()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FacebookRegisterButton {

            }

            Spacer(modifier = Modifier.width(20.dp))

            GoogleRegisterButton {

            }
        }
    }
}
