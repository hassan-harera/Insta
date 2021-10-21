package com.harera.feed

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.harera.feed.post.PostListView
import com.harera.model.modelget.Post
import com.harera.base.navigation.HomeNavigation

@ExperimentalCoilApi
@Composable
fun HomeFeed(
    feedViewModel: FeedViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state = feedViewModel.state.collectAsState().value
    val intent = remember { FeedIntent.FetchPosts }

    LaunchedEffect(intent) {
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

        is FeedState.LoadingMore -> {
            HomeFeedContent(
                posts = feedViewModel.posts.value,
                loadingMore = state.state,
                navController = navController
            )
        }

        is FeedState.Posts -> {
            HomeFeedContent(
                posts = feedViewModel.posts.value,
                navController = navController
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
    loadingMore: Boolean = true,
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

        if (loadingMore) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            val progress by animateLottieCompositionAsState(composition)

            Box {
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                )
            }
        }
    }
}
