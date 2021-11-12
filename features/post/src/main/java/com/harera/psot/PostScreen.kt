package com.harera.psot

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.state.PostState
import com.harera.base.theme.Orange158
import com.harera.compose.CommentView
import com.harera.model.model.Comment
import com.harera.model.response.PostResponse
import com.harera.post.PostCard
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val TAG = "PostScreen"

@ExperimentalCoilApi
@Composable
fun PostScreen(
    postViewModel: PostViewModel = getViewModel(),
    postId: Int,
    navController: NavHostController,
) {
    var post = remember<PostResponse?> { null }
    val scope = rememberCoroutineScope()
    val state = postViewModel.state

    Log.d(TAG, "PostScreen: $postId")

    LaunchedEffect(true) {
        postViewModel.sendIntent(PostIntent.FetchPost(postId = postId))
    }

    when (state) {
        is PostState.Error -> {

        }

        is PostState.Loading -> {

        }

        is PostState.PostFetched -> {
            post = state.post
        }
    }

    post?.let {
        PostView(
            post = post,
            comments = post.comments,
            onProfileClicked = {
                navController.navigate("${HomeNavigationRouting.VisitProfile}/${post.user.username}") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onLikeClicked = {
                scope.launch {
                    postViewModel.sendIntent(PostIntent.LikePost(postId))
                }
            },
            onCommentSubmitted = { comment ->
                scope.launch {
                    postViewModel.sendIntent(
                        PostIntent.CommentToPost(
                            comment = comment,
                            postId = postId
                        )
                    )
                }
            }
        )
    }
}

@ExperimentalCoilApi
@Composable
fun PostView(
    post: PostResponse,
    comments: List<Comment>,
    onProfileClicked: (String) -> Unit,
    onLikeClicked: () -> Unit,
    onCommentSubmitted: (String) -> Unit,
) {
    var commentFieldState by remember { mutableStateOf(false) }
    var expand by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(state = scrollState)
    ) {
        PostCard(
            postResponse = post,
            onProfileClicked = onProfileClicked,
            onPostClicked = {}
        )

        comments.forEach {
            CommentView(comment = it)
        }
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun PostViewPreview() {
//    PostView(
//        DummyDate.POST,
//        emptyList(),
//        onProfileClicked = {},
//        onLikeClicked = {},
//        onCommentSubmitted = {}
//    )
}

@Composable
private fun PostTopBar(postViewModel: PostViewModel) {
    var searchWord by remember { mutableStateOf("") }

    TopAppBar(
        backgroundColor = Orange158,
        contentColor = Orange158,
        title = {},
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        actions = {
            TextField(
                value = searchWord,
                onValueChange = {
                    searchWord = it
                },
                label = {
                    Text(text = "Search Any Thing")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Orange158,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    textColor = Color.White,
                    trailingIconColor = Color.Black
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
//                        TODO Adding search feature
//                        postViewModel.search()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        },
    )
}
