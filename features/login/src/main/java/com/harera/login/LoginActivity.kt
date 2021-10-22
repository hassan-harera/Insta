package com.harera.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.harera.base.utils.afterTextChanged
import com.harera.login.databinding.ActivityLoginBinding

@ExperimentalCoilApi
@ExperimentalComposeUiApi
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var bind: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }

    override fun onResume() {
        super.onResume()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        bind.password.setText(loginViewModel.password.value)

        bind.email.setText(loginViewModel.email.value)

        loginViewModel.loading.observe(this) {
            handleLoading()
        }

        loginViewModel.exception.observe(this) {
            handleFailure(exception = it)
        }

        loginViewModel.loginSuccess.observe(this) {
            if (it) {
                bind.loading.visibility = View.GONE
                goToHomeActivity()
            }
        }

        loginViewModel.formValidity.observe(this) {
            bind.login.isEnabled = it.isValid

            if (it.emailError != null) {
                bind.email.error = getString(it.emailError!!)
            } else if (it.passwordError != null) {
                bind.password.error = getString(it.passwordError!!)
            }
        }
    }

    private fun handleFailure(exception: Exception?) {
        exception?.printStackTrace()
        Toast.makeText(baseContext, exception?.message, Toast.LENGTH_SHORT).show()
        bind.loading.visibility = View.GONE
    }

    private fun handleLoading() {
        bind.loading.visibility = View.VISIBLE
    }

    private fun goToHomeActivity() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setupListeners() {
        bind.email.afterTextChanged {
            loginViewModel.setEmail(it)
        }

        bind.password.afterTextChanged {
            loginViewModel.setPassword(it)
        }

        bind.login.setOnClickListener {
            loginViewModel.login()
            bind.login.isEnabled = false
        }
    }
}