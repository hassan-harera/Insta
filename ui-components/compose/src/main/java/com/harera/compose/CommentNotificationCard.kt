package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
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
import com.harera.model.response.Notification
import com.harera.base.theme.White
import com.harera.time.TimeUtils

@Composable
@Preview
fun CommentList() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            item {
            }
            item {
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun CommentNotificationCard(
    commentNotification: Notification,
    onNotificationClicked: (postId: Int) -> Unit,
) {
    val text = "${commentNotification.profileName} ".plus(
        if (commentNotification.commentCount!! > 0)
            "and ${commentNotification.commentCount} others "
        else
            ""
    ).plus("commented to your post")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp)
            .clickable {
                onNotificationClicked(commentNotification.postId)
            },
        elevation = 5.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = (Alignment.Top)
        ) {

            Image(
                painter = rememberImagePainter(data = commentNotification.postImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
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
                        color = MaterialTheme.colors.primary,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = TimeUtils.timeFromNow(commentNotification.time),
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }
    }
}
