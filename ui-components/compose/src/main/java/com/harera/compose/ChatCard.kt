package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.harera.base.theme.White
import com.harera.base.theme.timeSize
import com.harera.model.response.ChatResponse
import com.harera.time.TimeUtils

@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun ChatCardPreview() {
    val chatResponse = Gson().fromJson(
        "    {\n" +
                "        \"username\": \"2\",\n" +
                "        \"name\": \"hassan\",\n" +
                "        \"lastMessage\": \"Hi\",\n" +
                "        \"time\": \"2021-11-10T19:55:17.000+02:00\"\n" +
                "    }",
        ChatResponse::class.java
    )


    ChatCard(
        chatResponse
    ) {

    }
}

@ExperimentalCoilApi
@Composable
fun ChatCard(
    chat: ChatResponse,
    onChatClicked: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChatClicked(chat.username)
            }
            .fillMaxHeight(0.15f)
            .padding(top = 3.dp, bottom = 3.dp),
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = (Alignment.CenterVertically)
        ) {
            Image(
                painter = if (chat.userImageUrl != null)
                    rememberImagePainter(chat.userImageUrl)
                else
                    painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
                    .clip(CircleShape),
                alignment = Alignment.CenterStart,
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = MaterialTheme.colors.primary,
                        text = chat.name,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Normal,
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )

                    Text(
                        text = TimeUtils.timeFromNow(chat.time),
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primary,
                            fontStyle = FontStyle.Normal,
                        ),
                        modifier = Modifier.fillMaxHeight(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primaryVariant,
                    )
                }

                Text(
                    text = chat.lastMessage,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = timeSize,
                        fontStyle = FontStyle.Italic,
                    ),
                    maxLines = 1,
                    color = MaterialTheme.colors.primaryVariant,
                )
            }
        }
    }
}
