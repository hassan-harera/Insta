package com.harera.base.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.State

open class BaseViewModel<T : State>(
   userSharedPreferences: LocalStore,
) : LocalStoreViewModel(userSharedPreferences) {

    var state by mutableStateOf<State>(State.Idle)

    fun handleFailure(exception: Exception?) {
        state = State.Error(exception)
        exception?.printStackTrace()
    }

    fun handleFailure(throwable: Throwable) {
        state = State.Error(Exception(throwable))
        throwable.printStackTrace()
    }

    fun updateLoading(state: Boolean) {
        this.state = State.Loading(state)
    }
}