package com.harera.mychats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.harera.base.navigation.chat.ChatsNavigation.ALL_CHATS
import com.harera.base.navigation.chat.ChatsNavigation.CHAT
import com.harera.base.theme.InstaTheme
import com.harera.compose.ChatCard
import com.harera.model.response.ChatResponse
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun MyChats(
    chatViewModel: MyChatsViewModel = getViewModel(),
    navController: NavHostController,
) {
    var chats by remember { mutableStateOf(emptyList<ChatResponse>()) }
    val state = chatViewModel.state

    LaunchedEffect(key1 = true) {
        chatViewModel.intent.send(ChatIntent.GetChats)
    }

    when (state) {
        is MyChatsState.Chats -> {
            chats = state.connections
        }

        else -> {

        }
    }

    InstaTheme {
        Scaffold(
            topBar = {
                MyChatsTopBar {
                    navController.popBackStack()
                }
            },
            floatingActionButton = {
                AddFloatingActionButton {
                    navController.navigate(ALL_CHATS) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            MyChatsContent(chats = chats) {
                navController.navigate("$CHAT/$it") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MyChatsContent(
    chats: List<ChatResponse>,
    onChatClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top
        ) {
            chats.forEach { chat ->
                item {
                    ChatCard(
                        chat = chat,
                        onChatClicked = {
                            onChatClicked(it)
                        }
                    )
                }
            }
        }
    }
}