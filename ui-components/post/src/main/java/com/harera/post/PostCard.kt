package com.harera.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.harera.base.theme.*
import com.harera.base.utils.time.TimeUtils
import com.harera.components.post.R
import com.harera.repository.data.DummyDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Preview
@Composable
fun PostCardPreview() {
    PostCard(
        DummyDate.POST,
        onProfileClicked = {},
        onPostClicked = {},
    )
}

@ExperimentalCoilApi
@Composable
fun PostCard(
    post: PostDetails,
    postViewModel: PostViewModel = getViewModel(),
    onProfileClicked: (String) -> Unit,
    onPostClicked: (String) -> Unit,
) {
    val state = postViewModel.state
    val scope = rememberCoroutineScope()

    when (state) {
        is PostState.Idle -> {

        }

        is PostState.Error -> {

        }

        is PostState.CommentAdded -> {

        }

        is PostState.PostFetched -> {

        }
    }

    PostCardContent(
        onProfileClicked = onProfileClicked,
        onPostClicked = onPostClicked,
        post = post,
        whenCommentSubmitted = { comment, postId ->
            scope.launch {
                postViewModel.sendIntent(
                    PostIntent.CommentToPost(
                        postId = postId,
                        comment = comment,
                    )
                )
            }
        },
        whenLikeClicked = { postId ->
            scope.launch {
                postViewModel.sendIntent(
                    PostIntent.LikePost(
                        postId = postId,
                    )
                )
            }
        }
    )
}

@ExperimentalCoilApi
@Composable
fun PostCardContent(
    post: PostDetails,
    onProfileClicked: (String) -> Unit,
    onPostClicked: (String) -> Unit,
    whenCommentSubmitted: (comment: String, postId: String) -> Unit,
    whenLikeClicked: (String) -> Unit,
) {
    var comment by remember { mutableStateOf("") }
    var commentFieldState by remember { mutableStateOf(false) }
    var dropDownState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onPostClicked(post.postId)
            },
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .background(Color.White),
            ) {
                IconButton(
                    onClick = {
                        dropDownState = true
                    },
                ) {
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                            }
                            .padding(6.dp),
                        tint = Color.Black,
                    )
                }


                DropdownMenu(
                    expanded = dropDownState,
                    onDismissRequest = {
                        dropDownState = false
                    },
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Text(
                        text = "Delete",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                Row {
                    Image(
                        painter = rememberImagePainter(post.profileImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                onProfileClicked(post.uid)
                            }
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Column {
                        Text(
                            //TODO change text value
                            text = post.profileName,
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontStyle = FontStyle.Normal,
                            ),
                            fontWeight = FontWeight.Bold,
                            maxLines = 4
                        )

//                Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            //TODO change text value
                            text = TimeUtils.timeFromNow(post.time),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = timeSize,
                                color = Grey660,
                                fontStyle = FontStyle.Normal,
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = post.caption,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = captionSize,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Image(
                    painter = rememberImagePainter(post.postImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    alpha = 1.0f,
                    alignment = Alignment.Center
                )

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
                            start = 5.dp,
                            end = 5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier
                            .weight(0.5f, true)
                            .fillMaxHeight()
                            .clickable {
                                onPostClicked(post.postId)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = null,
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(
                            modifier = Modifier.fillMaxHeight(),
                            textAlign = TextAlign.Center,
                            text = "${post.likesNumber}",
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(0.5f, true)
                            .fillMaxHeight()
                            .clickable { },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${post.commentsNumber} Comment"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
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
                        .height(10.dp)
                )

                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                whenLikeClicked(post.postId)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
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

                    Row(
                        modifier = Modifier
                            .clickable {
                                commentFieldState = true
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
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

                    Row(
                        modifier = Modifier
                            .clickable {

                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
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

                if (commentFieldState)
                    OutlinedTextField(
                        value = comment,
                        onValueChange = {
                            comment = it
                        },
                        placeholder = {
                            Text(text = "Comment")
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(8.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                commentFieldState = false
                                whenCommentSubmitted(
                                    comment,
                                    post.postId
                                )
                            }
                        )
                    )

            }
        }
    }

}
