package com.harera.chat_navigaton

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.harera.add_chat.NewChat
import com.harera.base.navigation.chat.ChatsNavigation
import com.harera.base.utils.Arguments
import com.harera.chat.ChatScreen
import com.harera.mychats.MyChats

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun HomeChats() {
    val navController = rememberNavController()

    NavHost(
        startDestination = ChatsNavigation.MY_CHATS,
        navController = navController,
    ) {
        composable(ChatsNavigation.MY_CHATS) {
            MyChats(
                navController = navController,
            )
        }

        composable(ChatsNavigation.ALL_CHATS) {
            NewChat(
                navController = navController
            )
        }

        composable("${ChatsNavigation.CHAT}/{${Arguments.UID}}") {
            ChatScreen(
                navController = navController,
                username = it.arguments!!.getString(Arguments.UID)!!,
            )
        }
    }
}

