package com.whiteside.insta.ui.chats

import com.whiteside.insta.R

sealed class ChatsNavigationIcons(var icon: Int, var label: String, var route: String) {
    object MyChats : ChatsNavigationIcons(
        R.drawable.chat,
        "My Chats",
        ChatsNavigation.MY_CHATS
    )

    object AllChats : ChatsNavigationIcons(
        R.drawable.plus,
        "New Chat",
        ChatsNavigation.ALL_CHATS
    )
}