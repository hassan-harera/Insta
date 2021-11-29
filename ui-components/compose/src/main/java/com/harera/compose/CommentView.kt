package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.harera.base.coil.CoilLoader
import com.harera.base.theme.timeSize
import com.harera.model.model.Comment
import com.harera.time.TimeUtils
import org.koin.androidx.compose.get

@ExperimentalCoilApi
@Composable
fun CommentView(
    comment: Comment,
    coilLoader: CoilLoader = get(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, bottom = 3.dp),
        elevation = 3.dp
    ) {
        Box(
            modifier = Modifier.padding(6.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 15.dp),
            ) {
                Image(
                    painter = rememberImagePainter(coilLoader.imageRequest(comment.userImageUrl)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            //TODO Not Implemented
                        }
                        .align(Top),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(MaterialTheme.colors.secondaryVariant,
                            shape = RoundedCornerShape(30))
                ) {
                    Text(
                        text = comment.profileName,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary,
                            fontStyle = FontStyle.Normal,
                        ),
                        fontWeight = FontWeight.Bold,
                        maxLines = 4,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 4.dp, end = 8.dp),
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = comment.comment,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            fontStyle = FontStyle.Normal,
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, top = 2.dp, end = 8.dp, bottom = 6.dp),
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                    )
                }
            }

            Text(
                text = TimeUtils.timeFromNow(comment.time),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = timeSize,
                    color = MaterialTheme.colors.primaryVariant,
                    fontStyle = FontStyle.Normal,
                ),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(CenterEnd)
                    .fillMaxWidth(0.1f),
            )
        }
    }
}