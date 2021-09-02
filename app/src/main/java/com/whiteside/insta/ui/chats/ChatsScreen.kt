package com.whiteside.insta.ui.chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.whiteside.insta.ui.chat.ChatScreen
import com.whiteside.insta.ui.utils.Arguments

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun HomeChats(
    chatViewModel: ChatViewModel
) {
    val navController = rememberNavController()

    NavHost(
        startDestination = ChatsNavigation.MY_CHATS,
        navController = navController,
    ) {
        composable(ChatsNavigation.MY_CHATS) {
            MyChats(
                chatViewModel = chatViewModel,
                navController = navController,
            )
        }

        composable(ChatsNavigation.ALL_CHATS) {
            AllChats(
                chatViewModel = chatViewModel,
                navController = navController
            )
        }

        composable("${ChatsNavigation.CHAT}/{${Arguments.UID}}") {
            ChatScreen(
                chatViewModel = chatViewModel,
                navController = navController,
                uid = it.arguments!!.getString(Arguments.UID)!!,
            )
        }
    }
}

