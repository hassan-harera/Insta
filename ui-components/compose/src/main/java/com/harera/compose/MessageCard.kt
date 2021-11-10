package com.harera.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.harera.base.DummyDate
import com.harera.base.theme.Grey200
import com.harera.model.model.Message

@ExperimentalCoilApi
@Composable
fun MessageList(messages: List<Message>, chatOwnerId: String) {

    LazyColumn(Modifier.fillMaxSize()) {
        messages.forEachIndexed { id, message ->
            item {
                MessageCard(
                    message = message,
                    chatOwnerId = chatOwnerId
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MessageCard(message: Message, chatOwnerId: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment =
        if (message.sender == chatOwnerId)
            Alignment.TopEnd
        else
            Alignment.TopStart
    ) {
        Text(
            text = message.message,
            Modifier
                .background(
                    color =
                    if (message.sender == chatOwnerId)
                        Color(0xE0579CE2)
                    else Grey200,
                    shape = RoundedCornerShape(CornerSize(8.dp)),
                )
                .padding(8.dp),
        )
    }
}

@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun MessageListPreview() {
    MessageList(
        chatOwnerId = "uid",
        messages = arrayListOf(
            DummyDate.MESSAGE,
            DummyDate.MESSAGE,
            DummyDate.MESSAGE,
            DummyDate.MESSAGE,
            DummyDate.MESSAGE,
        )
    )
}