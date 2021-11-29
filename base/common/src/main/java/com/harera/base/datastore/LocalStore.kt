package com.harera.base.datastore

import android.content.Context

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