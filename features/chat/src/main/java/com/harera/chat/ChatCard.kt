package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.harera.base.theme.Grey660
import com.harera.base.theme.timeSize
import com.harera.base.utils.TimeUtils
import com.harera.model.modelget.OpenChat
import com.harera.repository.data.DummyDate

@ExperimentalCoilApi
@Composable
@Preview
fun ChatCardPreview() {
    ChatCard(
        DummyDate.OPEN_CHAT,
        {}
    )
}

@ExperimentalCoilApi
@Composable
fun ChatCard(
    openChat: OpenChat,
    onChatClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f),
        elevation = 5.dp,
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = (Alignment.CenterVertically)
        ) {
            Image(
                //TODO replace image painter with link
                painter = rememberImagePainter(openChat.profileImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .padding(5.dp)
                    .border(3.dp, color = Grey660),
                alignment = Alignment.CenterStart
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        //TODO change text value
                        text = openChat.profileName,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Normal,
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )

                    Text(
                        text = TimeUtils.timeFromNow(openChat.time),
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            color = Grey660,
                            fontStyle = FontStyle.Normal,
                        ),
                        modifier = Modifier.fillMaxHeight(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                    )
                }

                Text(
                    text = openChat.lastMessage,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = timeSize,
                        color = Grey660,
                        fontStyle = FontStyle.Italic,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
