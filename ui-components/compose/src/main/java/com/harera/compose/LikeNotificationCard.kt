package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.harera.base.theme.Orange158
import com.harera.model.response.Notification
import com.harera.time.TimeUtils

private val notification: Notification = Gson().fromJson(
    "    {\n" +
            "        \"type\": 1,\n" +
            "        \"time\": \"2021-11-13T10:25:05\",\n" +
            "        \"likeCount\": 2,\n" +
            "        \"postId\": 23,\n" +
            "        \"postImageUrl\": \"http://192.168.1.15:8080/images/posts/17.jpg\",\n" +
            "        \"profileName\": \"hassan\"\n" +
            "    }",
    Notification::class.java)

@ExperimentalCoilApi
@Composable
@Preview
fun LikeList() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            LikeCard(likeNotification = notification) {}
            LikeCard(likeNotification = notification) {}
            LikeCard(likeNotification = notification) {}
        }
    }
}

@ExperimentalCoilApi
@Composable
fun LikeCard(
    likeNotification: Notification,
    onNotificationClicked: (postId: Int) -> Unit,
) {
    val text = (
            likeNotification.profileName
                .plus(
                    if (likeNotification.likeCount != 0)
                        " and ${likeNotification.likeCount} others"
                    else
                        ""
                )
                .plus(
                    " has liked your post"
                )
            )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNotificationClicked(likeNotification.postId)
            },
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = (Alignment.Top)
        ) {

            Image(
                painter = rememberImagePainter(data = likeNotification.postImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = TimeUtils.timeFromNow(likeNotification.time),
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        color = Orange158,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }
    }
}
