package com.harera.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.utils.Arguments
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun HomeChats(
    chatViewModel: ChatViewModel = getViewModel()
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

