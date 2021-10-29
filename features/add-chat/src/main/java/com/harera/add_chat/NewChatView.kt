package com.harera.add_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.harera.base.navigation.chat.ChatsNavigation
import com.harera.model.model.Profile
import com.harera.compose.ProfileCard
import com.harera.repository.data.DummyDate
import org.koin.androidx.compose.getViewModel

@Composable
fun NewChat(
    chatViewModel: NewChatViewModel = getViewModel(),
    navController: NavHostController
) {
    val state = chatViewModel.newChatState.collectAsState().value
    var intent = remember<NewChatIntent> { NewChatIntent.Free }
    var connections = remember<List<Profile>> { emptyList() }

    LaunchedEffect(intent) {
        chatViewModel.newChatIntent.send(intent)
    }

    intent = NewChatIntent.GetConnections

    when (state) {
        is NewChatState.Connections -> {
            connections = state.profiles
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
    connections: List<Profile>,
    navController: NavHostController
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
                        profile = it,
                        onClick = { uid ->
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
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun AllChatsPreview() {
    arrayListOf(
        DummyDate.PROFILE,
        DummyDate.PROFILE,
        DummyDate.PROFILE,
        DummyDate.PROFILE,
        DummyDate.PROFILE,
    ).let {
        LazyColumn(
            verticalArrangement = Arrangement.Top
        ) {
            it.forEach {
                item {
                    ProfileCard(
                        profile = it,
                        onClick = {}
                    )
                }
            }
        }
    }
}

