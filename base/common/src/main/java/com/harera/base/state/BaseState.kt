package com.harera.base.state

open class BaseState {
    data class Error(val data: Any? = null) : BaseState()
    data class Loading(val data: Any? = null) : BaseState()
    object Idle : BaseState()
}
