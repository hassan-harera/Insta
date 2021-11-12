package com.harera.base.state

open class State {
    data class Error(val data: Any? = null) : State()
    data class Loading(val data: Any? = null) : State()
    object Idle : State()
}
