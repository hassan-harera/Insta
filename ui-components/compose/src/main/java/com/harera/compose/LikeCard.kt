package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.harera.base.theme.Grey660
import com.harera.base.utils.TimeUtils
import com.harera.model.modelget.Like

@Composable
@Preview
fun LikeList() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            item {
                LikeCard(Like(), {})
            }
            item {
                LikeCard(Like(), {})
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun LikeCard(
    like: Like,
    onNotificationClicked: (postId : String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNotificationClicked(like.postId)
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
                //TODO replace image painter with link
                painter = rememberImagePainter(data = like.postImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(3.dp, color = Grey660)
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${like.profileName} Liked your Post",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = TimeUtils.timeFromNow(like.time),
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        color = Grey660,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }
    }
}
