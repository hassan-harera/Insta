package com.harera.chat

import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.gson.Gson
import com.harera.base.state.BaseState
import com.harera.base.state.ChatState
import com.harera.compose.Toast
import com.harera.model.model.User
import com.harera.model.response.MessageResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val TAG = "ChatScreen"

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun ChatScreen(
    username: String,
    chatViewModel: ChatViewModel = getViewModel(),
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val state = chatViewModel.state
    val context = LocalContext.current
    var profile by remember { mutableStateOf<User?>(null) }
    var messages by remember { mutableStateOf<List<MessageResponse>>(emptyList()) }

    LaunchedEffect(true) {
        chatViewModel.intent.send(ChatIntent.GetProfile(username))
        delay(400)
        chatViewModel.intent.send(ChatIntent.GetMessages(username))
    }

    (context as AppCompatActivity)
        .registerReceiver(
            MessagesBroadcast {
                messages = messages.plus(Gson().fromJson(it, MessageResponse::class.java))
            },
            IntentFilter(ServiceUtil.NEW_MESSAGE)
        )

    Log.d(TAG, "ChatScreen: $profile")
    when (state) {
        is ChatState.ProfileState -> {
            profile = state.user
        }

        is ChatState.Messages -> {
            messages = state.messages
        }

        is BaseState.Error -> {
            Toast(message = state.data.toString())
        }
    }

    ChatScreenContent(
        user = profile,
        messages = messages,
        navController = navController,
    ) {
        scope.launch {
            chatViewModel.intent.send(ChatIntent.SendMessage(message = it, connection = username))
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun ChatScreenContent(
    user: User?,
    navController: NavHostController,
    messages: List<MessageResponse> = emptyList(),
    sendMessage: (message: String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            user?.let {
                ChatTopBar(
                    user = user,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        },
        bottomBar = {
            ChatBottomBar(
                message = message,
                onMessageChanged = {
                    message = it
                },
                onMessageSendClicked = {
                    keyboardController?.hide()
                    sendMessage(message)
                    message = ""
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        messages.let {
            MessageList(messages = it, paddingValues)
        }
    }
}