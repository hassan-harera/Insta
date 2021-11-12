package com.harera.base.datastore

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class LocalStore(val context: Context) {

    private val userSP = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    companion object {
        const val USERNAME = ("username")
        const val TOKEN = "token"
    }

    fun getUsername() = userSP.getString(USERNAME, null)
    fun updateUsername(username: String) =
        userSP.edit().putString(USERNAME, username).commit()

    fun getToken() = userSP.getString(TOKEN, null)
    fun updateToken(token: String) = userSP.edit().putString(TOKEN, token).commit()

}