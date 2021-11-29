package com.harera.psot

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.state.PostState
import com.harera.base.theme.White
import com.harera.compose.CommentView
import com.harera.model.model.Comment
import com.harera.model.response.PostResponse
import com.harera.post.ImagePostCard
import com.harera.post.TextPostCard
import org.koin.androidx.compose.getViewModel


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun PostScreen(
    postViewModel: PostViewModel = getViewModel(),
    postId: Int,
    navController: NavHostController,
) {
    var post = remember<PostResponse?> { null }
    val state = postViewModel.state

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
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun PostView(
    post: PostResponse,
    comments: List<Comment>,
    onProfileClicked: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(state = scrollState)
    ) {
        when (post.post.type) {
            1 -> {
                ImagePostCard(
                    postResponse = post,
                    onProfileClicked = onProfileClicked,
                    onPostClicked = {}
                )
            }

            2 -> {
                TextPostCard(
                    postResponse = post,
                    onProfileClicked = onProfileClicked,
                    onPostClicked = {}
                )
            }
        }

        comments.sortedByDescending { it.time }.forEach {
            CommentView(comment = it)
        }
    }
}

@Composable
private fun PostTopBar(postViewModel: PostViewModel) {
    var searchWord by remember { mutableStateOf("") }

    TopAppBar(
        backgroundColor = White,
        contentColor = White,
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
                    backgroundColor = White,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    textColor = Color.White,
                    trailingIconColor = Color.Black
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        },
    )
}
