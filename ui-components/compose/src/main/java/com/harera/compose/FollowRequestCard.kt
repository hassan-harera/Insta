package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harera.base.theme.Blue255
import com.harera.base.theme.Grey660
import com.harera.model.model.FollowRequest
import com.harera.model.model.Post
import com.harera.model.model.Profile

@Composable
@Preview
fun FollowRequestList() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            item {
                FollowRequest()
            }
            item {
                FollowRequest()
            }
        }
    }
}

@Composable
@Preview
fun FollowRequestCard() {
    val post by remember { mutableStateOf<Post?>(Post()) }
    val profile by remember {
        mutableStateOf<Profile?>(Profile())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            },
        elevation = 5.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = (Alignment.Top)
        ) {

            Image(
                //TODO replace image painter with link
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(3.dp, color = Grey660),
                alignment = Alignment.TopCenter
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                Text(
                    //TODO change text value
                    text = "You have a new follow request from Hassan Shaban",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Normal
                )

                Text(
                    //TODO change text value
                    text = "3 min",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        color = Grey660,
                        fontStyle = FontStyle.Italic,
                    ),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //TODO change text value
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(0.4f),
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Blue255
                        )
                    ) {
                        Text(
                            text = "Accept",
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                color = Color.White,
                                fontStyle = FontStyle.Normal,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.fillMaxWidth(0.15f))

                    //TODO change text value
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Grey660,
                            contentColor = Grey660,
                        ),

                        ) {
                        Text(
                            text = "Ignore",
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontStyle = FontStyle.Normal,
                            ),
                        )
                    }
                }
            }
        }
    }
}
