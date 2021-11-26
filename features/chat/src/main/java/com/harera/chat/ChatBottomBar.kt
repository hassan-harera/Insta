package com.harera.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.harera.base.theme.White


@ExperimentalComposeUiApi
@Composable
fun ChatBottomBar(
    message: String,
    onMessageChanged: (String) -> Unit,
    onMessageSendClicked: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BottomAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .fillMaxHeight()
                    .padding(2.dp)
                    .border(1.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(22.dp))
                    .align(Alignment.CenterStart),
                value = message,
                onValueChange = {
                    onMessageChanged(it)
                },
                placeholder = {
                    Text(text = "Message Content")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    focusedLabelColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = MaterialTheme.colors.background,
                    unfocusedLabelColor = MaterialTheme.colors.background,
                    unfocusedIndicatorColor = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.secondary,
                ),
            )

            FloatingActionButton(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.14f).align(Alignment.CenterEnd),
                onClick = {
                    keyboardController?.hide()
                    onMessageSendClicked(message)
                    onMessageChanged("")
                },
                shape = CircleShape,
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
            }
        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun ChatBottomBarPreview() {
    ChatBottomBar(
        "",
        {},
        {}
    )
}