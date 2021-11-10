package com.harera.add_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.harera.base.navigation.chat.ChatsNavigation
import com.harera.base.state.NewChatState
import com.harera.compose.ProfileCard
import com.harera.model.response.Connection
import org.koin.androidx.compose.getViewModel

@Composable
fun NewChat(
    chatViewModel: NewChatViewModel = getViewModel(),
    navController: NavHostController,
) {
    val state = chatViewModel.state
    var intent = remember<NewChatIntent> { NewChatIntent.Free }
    var connections = remember<List<Connection>> { emptyList() }

    LaunchedEffect(intent) {
        chatViewModel.newChatIntent.send(intent)
    }

    intent = NewChatIntent.GetConnections

    when (state) {
        is NewChatState.Connections -> {
            connections = state.connections
        }
        is NewChatState.Idle -> {

        }
    }

    AllChatsContent(
        navController = navController,
        connections = connections
    )
}

@Composable
fun AllChatsContent(
    connections: List<Connection>,
    navController: NavHostController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top
        ) {
            connections.forEach {
                item {
                    ProfileCard(
                        user = it
                    ) { uid ->
                        navController.navigate(
                            route = "${ChatsNavigation.CHAT}/${uid}"
                        ) {
                            popUpTo(ChatsNavigation.ALL_CHATS) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun AllChatsPreview() {
//    arrayListOf(
//        DummyDate.USER,
//        DummyDate.USER,
//        DummyDate.USER,
//        DummyDate.USER,
//        DummyDate.USER,
//    ).let {
//        LazyColumn(
//            verticalArrangement = Arrangement.Top
//        ) {
//            it.forEach {
//                item {
//                    ProfileCard(
//                        user = it
//                    ) {}
//                }
//            }
//        }
//    }
}

