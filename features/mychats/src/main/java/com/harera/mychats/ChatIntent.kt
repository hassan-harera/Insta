package com.harera.mychats

sealed class ChatIntent {
    object GetChats : ChatIntent()
}
