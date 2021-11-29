package com.harera.base.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.harera.base.datastore.LocalStore
import com.harera.base.state.BaseState

open class BaseViewModel<T : BaseState>(
    userSharedPreferences: LocalStore,
) : LocalStoreViewModel(userSharedPreferences) {

    var state by mutableStateOf<BaseState>(BaseState.Idle)

    fun handleFailure(exception: Exception?) {
        state = BaseState.Error(exception)
        exception?.printStackTrace()
    }

    fun handleFailure(throwable: Throwable) {
        state = BaseState.Error(Exception(throwable))
        throwable.printStackTrace()
    }

    fun updateLoading(state: Boolean) {
        this.state = BaseState.Loading(state)
    }
}