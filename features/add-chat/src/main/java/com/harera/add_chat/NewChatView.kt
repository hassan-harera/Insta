package com.harera.add_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harera.base.navigation.chat.ChatsNavigation
import com.harera.base.state.NewChatState
import com.harera.base.theme.InstaTheme
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

    InstaTheme {
        AllChatsContent(
            navController = navController,
            connections = connections
        )
    }
}

@Composable
fun AllChatsContent(
    connections: List<Connection>,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            ConnectionsTopBar {
                navController.popBackStack()
            }
        },
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
fun ConnectionsTopBar(
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(5.dp),

        ) {

        Icon(
            tint = MaterialTheme.colors.primary,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .width(35.dp)
                .padding(5.dp)
                .clickable {
                    onBackClicked()
                },
        )

        Text(
            text = "Connections",
            color = MaterialTheme.colors.primary,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            modifier = Modifier.padding(8.dp),
        )
    }
}