package com.harera.functional_programming

fun <T:Any?> T?.isNotNull(block: (T) -> Unit): T? {
    if (this != null) {
        block(this)
        return this
    }
    return null
}

fun <T:Any?> T?.isNull(block: () -> Unit): T? {
    if (this == null) {
        block()
        return null
    }
    return this
}