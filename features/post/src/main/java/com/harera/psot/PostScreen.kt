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
import com.harera.base.navigation.home.HomeNavigation
import com.harera.base.theme.Grey660
import com.harera.compose.CommentView
import com.harera.model.model.Comment
import com.harera.model.model.Post
import com.harera.post.PostCard
import com.harera.post.PostDetails
import com.harera.repository.data.DummyDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@ExperimentalCoilApi
@Composable
fun PostScreen(
    postViewModel: PostViewModel = getViewModel(),
    postId: String,
    navController: NavHostController
) {
    var post = remember<PostDetails?> { null }
    var comments = remember<List<Comment>> { emptyList() }

    val scope = rememberCoroutineScope()

    val state = postViewModel.state

    LaunchedEffect(true) {
        postViewModel.apply {
            sendIntent(PostIntent.FetchPost(postId = postId))
            sendIntent(PostIntent.FetchPostLikes(postId = postId))
            sendIntent(PostIntent.FetchPostComments(postId = postId))
        }
    }

    when (state) {
        is PostState.Error -> {

        }

        is PostState.Loading -> {

        }

        is PostState.PostFetched -> {
            post = state.post
        }

        is PostState.CommentsFetched -> {
            comments = state.comments
        }
    }

    post?.let {
        Log.d("PostScreen", post.postId)
        PostView(
            post = post,
            comments = comments,
            onProfileClicked = {
                navController.navigate("${HomeNavigation.VisitProfile}/${post.uid}") {
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
    post: PostDetails,
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
            post = post,
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
    PostView(
        DummyDate.POST,
        emptyList(),
        onProfileClicked = {},
        onLikeClicked = {},
        onCommentSubmitted = {}
    )
}

@Composable
private fun PostTopBar(postViewModel: PostViewModel) {
    var searchWord by remember { mutableStateOf("") }

    TopAppBar(
        backgroundColor = Grey660,
        contentColor = Grey660,
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
                    backgroundColor = Grey660,
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
