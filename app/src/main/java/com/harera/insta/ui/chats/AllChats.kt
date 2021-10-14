package com.harera.insta.ui.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.harera.model.modelget.Profile
import com.harera.insta.ui.components.ProfileCard
import com.harera.insta.ui.data.DummyDate

@Composable
fun AllChats(
    chatViewModel: ChatViewModel,
    navController: NavHostController
) {
    chatViewModel.getAllConnections()
    val connections by chatViewModel.connectionProfiles

    AllChatsContent(
        navController = navController,
        chatViewModel = chatViewModel,
        connections = connections
    )
}

@Composable
fun AllChatsContent(
    connections: List<Profile>,
    chatViewModel: ChatViewModel,
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

