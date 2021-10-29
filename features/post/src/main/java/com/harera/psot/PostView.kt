package com.harera.psot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harera.base.theme.*
import com.harera.model.model.Post
import com.harera.model.model.Profile

@Composable
@Preview
private fun PostScreen() {
    Scaffold(
        Modifier
            .fillMaxSize(),

        topBar = {
            PostTopBar()
        },
    ) {
        Body()
    }
}

@Preview
@Composable
private fun Body() {
    val post by remember { mutableStateOf<Post?>(Post()) }
    val profile by remember {
        mutableStateOf<Profile?>(Profile())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White),

        ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(3.dp, color = Grey660)
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                profile?.let {
                    Text(
                        //TODO change text value
                        text = "Hassan Shaban",
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Normal,
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }

//                Spacer(modifier = Modifier.size(4.dp))

                post?.let {
                    Text(
                        //TODO change text value
                        text = "3 min",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = timeSize,
                            color = Color.Black,
                            fontStyle = FontStyle.Normal,
                        ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        post?.let {
            Text(
                //TODO change text value
                text = "I Went to the sea in a very nice trip",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = captionSize,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Image(
                //TODO change text value
                painter = painterResource(id = R.drawable.sea),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                alpha = 1.0f,
                alignment = Alignment.Center
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(15.dp)
                .padding(
                    start = 10.dp,
                    end = 10.dp
                ),
            verticalAlignment = CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                onClick = { /*TODO*/ },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        textAlign = TextAlign.Center,
                        text = "369"
                    )
                }
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 5.dp)
                    .fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    textAlign = TextAlign.End,
                    text = "369 CommentToPost"
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .border(width = 1.dp, color = Grey660)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(0.3f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        textAlign = TextAlign.Center,
                        text = like
                    )
                }
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(0.3f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.comment),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = commment,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(0.3f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        text = share,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PostTopBar() {
    var searchWord by remember { mutableStateOf("") }

    TopAppBar(
        modifier = Modifier.padding(0.dp),
        backgroundColor = Grey660,
        contentColor = Grey660,
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        title = {
            TextField(
                value = searchWord,
                onValueChange = {
                    searchWord = it
                },
                label = {
                    Text(
                        text = "Search Any Thing",
                        textAlign = TextAlign.End
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    textColor = Color.White,
                    trailingIconColor = Color.Black,
                    backgroundColor = Color.Unspecified
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        //TODO call search method
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
            )
        },
    )
}