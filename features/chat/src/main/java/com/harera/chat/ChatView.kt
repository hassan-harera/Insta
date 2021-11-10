package com.harera.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.harera.base.DummyDate
import com.harera.base.navigation.chat.NavigationIcon
import com.harera.base.state.ChatState
import com.harera.base.theme.Grey300
import com.harera.base.theme.Grey60
import com.harera.base.theme.Grey700
import com.harera.model.model.Message
import com.harera.model.model.User
import org.koin.androidx.compose.getViewModel

private const val TAG = "ChatScreen"

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun ChatScreen(
    uid: String,
    chatViewModel: ChatViewModel = getViewModel(),
    navController: NavHostController,
) {
    val state = chatViewModel.state
    var intent = remember<ChatIntent> { ChatIntent.Free }
    var profile = remember<User?> { null }
    var messages = remember<List<Message>> { emptyList() }

    LaunchedEffect(intent) {
        chatViewModel.intent.send(intent)
    }

    intent = ChatIntent.GetProfile(uid)
    intent = ChatIntent.GetMessages(uid)

    when (state) {
        is ChatState.ProfileState -> {
            profile = state.user
        }

        is ChatState.Messages -> {
            messages = state.messages
        }
    }

    ChatScreenContent(
        user = profile,
        messages = messages,
        navController = navController,
        receiverUID = uid,
    ) {
        intent = ChatIntent.SendMessage(message = it)
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun ChatScreenContent(
    user: User?,
    navController: NavHostController,
    messages: List<Message> = emptyList(),
    receiverUID: String,
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
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.fillMaxWidth(0.15f),
                onClick = {
                    keyboardController?.hide()
                    sendMessage(message)
                    message = ""
                },
                shape = CircleShape,
                backgroundColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        messages.let {
            MessageList(messages = it)
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ChatBottomBar(
    message: String,
    onMessageChanged: (String) -> Unit,
    onMessageSendClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = Grey300,
        elevation = 1.dp,
        cutoutShape = CircleShape
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight(),
            value = message,
            onValueChange = {
                onMessageChanged(it)
            },
            placeholder = {
                Text(text = "Message Content")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Unspecified,
                focusedLabelColor = Grey700,
                focusedIndicatorColor = Grey700,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    onMessageSendClicked()
                }
            )
        )
    }
}

@ExperimentalCoilApi
@Composable
fun ChatTopBar(
    user: User,
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.background(Grey60),
        contentPadding = PaddingValues(4.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        NavigationIcon(
            onClick = {
                onBackClicked()
            },
            buttonColor = Color.Black
        )

        Image(
            painter = rememberImagePainter(user.userImageUrl),
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
                .size(40.dp)
                .clip(CircleShape),
            alignment = Alignment.CenterStart
        )

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            //TODO change text value
            text = user.name,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Normal,
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            maxLines = 1,
            color = Color.Black
        )
    }
}

@ExperimentalCoilApi
@Composable
@Preview
fun ChatTopBarPreview() {
    ChatTopBar(user = DummyDate.USER, {})
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
@Preview
fun ChatBottomBarPreview() {
    ChatBottomBar(
        "",
        {},
        {}
    )
}