package com.harera.feed

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.model.modelget.Post
import com.harera.navigation.HomeNavigation
import com.harera.post.PostListView

@ExperimentalCoilApi
@Composable
fun HomeFeed(
    feedViewModel: FeedViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state = feedViewModel.state.collectAsState().value
    val intent = remember { FeedIntent.FetchPosts }

    LaunchedEffect(intent) {
        Log.d("HomeFeed", "Intent Called")
        feedViewModel.intent.send(intent)
    }

    when (state) {
        is FeedState.Error -> {
            Toast.makeText(
                LocalContext.current,
                state.message ?: "",
                Toast.LENGTH_SHORT
            ).show()
        }

        is FeedState.Posts -> {
            Log.d("HomeFeed", "Posts observed")
            HomeFeedContent(
                state.postList,
                navController
            )
        }

        is FeedState.Loading -> {
            LoadingRecipeListShimmer(
                imageHeight = 300.dp,
                padding = 5.dp
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeFeedContent(
    posts: List<Post>,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = "write what you want")
            },
            modifier = Modifier
                .background(Color.White)
                .padding(2.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(HomeNavigation.AddPost) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
            ),
            enabled = false,
        )

        PostListView(
            posts = posts,
            navController = navController
        )

        if (posts.isEmpty()) {
            EmptyPostList()
        }
    }

}

@Composable
fun EmptyPostList() {
    Image(
        painter = painterResource(id = R.drawable.empty_list),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}
