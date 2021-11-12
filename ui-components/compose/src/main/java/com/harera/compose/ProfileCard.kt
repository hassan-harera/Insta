package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.harera.base.theme.Orange158
import com.harera.base.theme.timeSize
import com.harera.model.response.Connection

@Composable
@Preview
fun ProfileCardPreview() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        item {
//            ProfileCard(DummyDate.USER) {}
        }
        item {
//            ProfileCard(DummyDate.USER) {}
        }
        item {
//            ProfileCard(DummyDate.USER) {}
        }
        item {
//            ProfileCard(DummyDate.USER) {}
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileCard(
    user: Connection,
    onClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .clickable {
                onClick(user.username)
            },
        elevation = 5.dp,
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = (Alignment.CenterVertically)
        ) {
            Image(
                painter = if (user.userImageUrl != null)
                    rememberImagePainter(user.userImageUrl)
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
                Text(
                    //TODO change text value
                    text = user.profileName,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )

                Text(
                    text = "username " + user.username,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = timeSize,
                        color = Orange158,
                        fontStyle = FontStyle.Italic,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

