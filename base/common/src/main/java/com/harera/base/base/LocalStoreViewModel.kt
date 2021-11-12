package com.harera.base.base

import androidx.lifecycle.ViewModel
import com.harera.base.datastore.LocalStore

open class LocalStoreViewModel(
    private val userSharedPreferences: LocalStore,
) : ViewModel() {
    var token = userSharedPreferences.getToken()
        private set

    fun updateToken(token: String) {
        userSharedPreferences.updateToken(token)
    }

    var username = userSharedPreferences.getUsername()
        private set

    fun updateUsername(username: String) {
        userSharedPreferences.updateUsername(username)
    }
}