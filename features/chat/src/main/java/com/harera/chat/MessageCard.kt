package com.harera.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.google.gson.Gson
import com.harera.base.base.LocalStoreViewModel
import com.harera.model.response.MessageResponse
import com.harera.time.TimeUtils.Companion.timeFromNow
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun MessageList(messages: List<MessageResponse>, paddingValues: PaddingValues) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(paddingValues)
    ) {
        messages.forEach { message ->
            MessageCard(
                message = message,
            )
        }

        LaunchedEffect(key1 = true) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MessageCard(
    message: MessageResponse,
    localStoreViewModel: LocalStoreViewModel = getViewModel(),
) {
    val isSender = localStoreViewModel.username == message.sender

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (isSender)
            Alignment.TopEnd
        else
            Alignment.TopStart
    ) {
        Card(
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentWidth(
                    align = if (isSender)
                        Alignment.End
                    else
                        Alignment.Start
                )
                .clip(shape = RoundedCornerShape(CornerSize(8.dp))),
            backgroundColor = if (isSender)
                MaterialTheme.colors.secondaryVariant
            else
                MaterialTheme.colors.primaryVariant,
        ) {
            Box{
                Text(
                    text = message.message,
                    Modifier
                        .padding(
                            if (isSender)
                                PaddingValues(start = 8.dp, end = 23.dp, bottom = 23.dp, top = 8.dp)
                            else
                                PaddingValues(start = 23.dp, end = 8.dp, bottom = 23.dp, top = 8.dp)
                        )
                        .align(
                            if (isSender)
                                Alignment.TopStart
                            else
                                Alignment.TopEnd
                        ),
                    color = MaterialTheme.colors.primary
                )

                Text(
                    text = timeFromNow(message.time),
                    modifier = Modifier
                        .padding(3.dp)
                        .align(
                            if (isSender)
                                Alignment.BottomEnd
                            else
                                Alignment.BottomStart
                        ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant,
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
@Preview
fun MessageListPreview() {
    Gson().fromJson("    {\n" +
            "        \"message\": \"message you when I get\",\n" +
            "        \"messageId\": 129,\n" +
            "        \"sender\": \"5\",\n" +
            "        \"receiver\": \"2\",\n" +
            "        \"time\": \"2021-11-22T22:36:25.000+02:00\"\n" +
            "    }",
        MessageResponse::class.java
    ).let {
        MessageList(
            messages = arrayListOf(
                it,
                it,
                it,
                it,
                it,
            ),
            paddingValues = PaddingValues()
        )
    }
}