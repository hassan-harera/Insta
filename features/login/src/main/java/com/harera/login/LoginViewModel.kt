package com.harera.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harera.base.utils.Validity
import com.harera.repository.db.network.abstract_.AuthManager

class LoginViewModel constructor(
    private val authManager: AuthManager,
) : ViewModel() {
    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private var _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private var _formValidity = MutableLiveData<LoginState>()
    val formValidity: LiveData<LoginState> = _formValidity

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginSuccess = MutableLiveData<Boolean>(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _exception = MutableLiveData<Exception?>()
    val exception: LiveData<Exception?> = _exception

    private fun checkFormValidity() {
        if (_email.value == null || !Validity.checkEmail(_email.value!!)) {
            _formValidity.value = LoginState(emailError = R.string.email_error)
        } else if (password.value == null || !Validity.checkPassword(password.value!!)) {
            _formValidity.value = LoginState(passwordError = R.string.password_error)
        } else {
            _formValidity.value = LoginState(isValid = true)
        }
    }

    fun setEmail(it: String) {
        _email.value = it
        checkFormValidity()
    }

    fun setPassword(it: String) {
        _password.value = it
        checkFormValidity()
    }

    fun login() {
        _loading.value = true
        authManager
            .signInWithEmailAndPassword(email = email.value!!, password = password.value!!)
            .addOnSuccessListener {
                _loading.value = false
                _loginSuccess.value = true
            }
            .addOnFailureListener {
                _loading.value = false
                _exception.value = it
            }
    }
}