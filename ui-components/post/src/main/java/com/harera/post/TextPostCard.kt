package com.harera.post

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.harera.base.DummyDate
import com.harera.base.base.LocalStoreViewModel
import com.harera.base.coil.CoilLoader
import com.harera.base.state.PostState
import com.harera.base.theme.InstaTheme
import com.harera.base.theme.share
import com.harera.base.theme.timeSize
import com.harera.components.post.R
import com.harera.model.response.PostResponse
import com.harera.time.TimeUtils.Companion.timeFromNow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Preview
@Composable
fun TextPostCardPreview() {
    ImagePostCard(
        DummyDate.POST_DETAILS,
        onProfileClicked = {},
        onPostClicked = {},
    )
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun TextPostCard(
    postResponse: PostResponse,
    postViewModel: ImagePostViewModel = get(),
    onProfileClicked: (String) -> Unit,
    onPostClicked: (Int) -> Unit,
) {
    val state = postViewModel.state
    val scope = rememberCoroutineScope()

    when (state) {
        is PostState.Idle -> {

        }

        is PostState.Error -> {

        }

        is PostState.CommentsFetched -> {
            postResponse.comments = state.comments
        }

        is PostState.LikesFetched -> {
            postResponse.likes = state.likes
        }

        is PostState.PostFetched -> {

        }
    }

    InstaTheme {
        PostCardContent(
            onProfileClicked = onProfileClicked,
            onPostClicked = onPostClicked,
            postDetails = postResponse,
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
            onLikeClicked = { postId ->
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
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun PostCardContent(
    postDetails: PostResponse,
    onProfileClicked: (String) -> Unit,
    onPostClicked: (Int) -> Unit,
    whenCommentSubmitted: (comment: String, postId: Int) -> Unit,
    onLikeClicked: (Int) -> Unit,
    localStoreViewModel: LocalStoreViewModel = getViewModel(),
    coilLoader: CoilLoader = get(),
) {
    var comment by remember { mutableStateOf("") }
    val username = localStoreViewModel.username
    var commentFieldState by remember { mutableStateOf(false) }
    var dropDownState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onPostClicked(postDetails.post.postId)
            },
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
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
                        tint = MaterialTheme.colors.secondary,
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
            ) {
                Row {
                    Image(
                        painter = rememberImagePainter(
                            coilLoader.imageRequest(postDetails.user.userImageUrl)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                onProfileClicked(postDetails.user.username)
                            }
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Column {
                        Text(
                            //TODO change text value
                            text = postDetails.user.name,
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontStyle = FontStyle.Normal,
                            ),
                            fontWeight = FontWeight.Bold,
                            maxLines = 4,
                            color = MaterialTheme.colors.primary
                        )

                        Text(
                            text = timeFromNow(postDetails.post.time),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = timeSize,
                                fontStyle = FontStyle.Normal,
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box {
                    Image(
                        painter = rememberImagePainter(
                            coilLoader.imageRequest(postDetails.post.postImageUrl)
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                    )

                    val scrollState = rememberScrollState()

                    Text(
                        text = postDetails.post.caption,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Normal,
                        ),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight()
                            .padding(all = 4.dp)
                            .align(Alignment.Center),
                        fontSize = 30.sp,
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
                        .padding(
                            start = 5.dp,
                            end = 5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable {
                                onPostClicked(postDetails.post.postId)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            textAlign = TextAlign.Center,
                            text = "${postDetails.likes.size}",
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable {

                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${postDetails.comments.size} Comment",
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                            fontSize = 12.sp
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
                        .border(width = 1.dp, color = MaterialTheme.colors.secondary)
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

                    LikeIcon(
                        isLiked = postDetails.likes.any { it.username == username },
                        onClicked = { onLikeClicked(postDetails.post.postId) }
                    )

                    CommentIcon(
                        onClicked = {
                            commentFieldState = true
                        }
                    )

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
                            .padding(8.dp)
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = true,
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                commentFieldState = false
                                whenCommentSubmitted(
                                    comment,
                                    postDetails.post.postId
                                )
                            }
                        )
                    )
            }
        }
    }
}
