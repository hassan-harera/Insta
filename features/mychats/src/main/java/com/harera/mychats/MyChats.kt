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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.DummyDate
import com.harera.base.navigation.chat.ChatsNavigation.ALL_CHATS
import com.harera.compose.ChatCard
import com.harera.model.model.OpenChat
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun MyChats(
    chatViewModel: MyChatsViewModel = getViewModel(),
    navController: NavHostController,
) {
    val connections by chatViewModel.openChats
    val state = chatViewModel.state


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.fillMaxWidth(0.15f),
                onClick = {
                    navController.navigate(ALL_CHATS) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                shape = CircleShape,
                backgroundColor = Color.White,
                contentColor = Color.Unspecified
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        MyChatsContent(
            openChats = connections
        )
    }
}

@ExperimentalCoilApi
@Composable
fun MyChatsContent(openChats: List<OpenChat>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top
        ) {
            openChats.forEach { openChat ->
                item {
                    ChatCard(
                        onChatClicked = {

                        },
                        openChat = openChat
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun MyChatsPreview() {
    MyChatsContent(
        openChats = arrayListOf(
            DummyDate.OPEN_CHAT,
            DummyDate.OPEN_CHAT,
            DummyDate.OPEN_CHAT,
            DummyDate.OPEN_CHAT,
        )
    )
}

