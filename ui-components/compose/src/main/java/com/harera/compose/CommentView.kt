package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Top
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
import coil.compose.rememberImagePainter
import com.harera.base.DummyDate.POST
import com.harera.base.theme.Orange158
import com.harera.base.theme.timeSize
import com.harera.model.model.Comment
import com.harera.time.TimeUtils

@Composable
fun CommentView(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 15.dp),
    ) {
        Image(
            painter = rememberImagePainter(POST.postImageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
                .clip(CircleShape)
                .clickable {
                    //TODO Not Implemented
                }
                .align(Top),
        )

        Column {
            Text(
                //TODO change text value
                text = comment.username,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                fontWeight = FontWeight.Bold,
                maxLines = 4
            )

            Text(
                //TODO change text value
                text = comment.comment,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                //TODO change text value
                text = TimeUtils.timeFromNow(comment.time),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = timeSize,
                    color = Orange158,
                    fontStyle = FontStyle.Normal,
                ),
            )
        }
    }
}

@Preview
@Composable
fun CommentPreview() {
//    TODO
//    CommentView(comment = COMMENT_DETAILS)
}