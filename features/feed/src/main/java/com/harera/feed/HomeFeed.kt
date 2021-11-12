package com.harera.feed

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.state.FeedState
import com.harera.model.response.PostResponse
import com.harera.post.PostListView
import org.koin.androidx.compose.getViewModel

private const val TAG = "HomeFeed"

@ExperimentalCoilApi
@Composable
fun HomeFeed(
    feedViewModel: FeedViewModel = getViewModel(),
    navController: NavHostController,
) {
    val state = feedViewModel.state
    val scope = rememberCoroutineScope()

    Log.d(TAG, "HomeFeed: ${state.javaClass}")

    LaunchedEffect(key1 = true) {
        Log.d(TAG, "HomeFeed: FetchPosts")
        feedViewModel.intent.send(FeedIntent.FetchPosts)
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
            LoadingRecipeListShimmer(
                imageHeight = 300.dp,
                padding = 5.dp
            )
        }

        is FeedState.Posts -> {
            HomeFeedContent(
                posts = state.posts,
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

@Composable
@Preview
fun TopFeedBarPreview() {
    TopFeedBar(
        {

        }, {

        }
    )
}

@Composable
fun TopFeedBar(
    onTextPostClicked: () -> Unit,
    onImagePostClicked: () -> Unit,
) {
    val topBar = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(topBar)
            .fillMaxWidth()
            .fillMaxHeight(0.3f),

    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight()
                .clickable {

                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = R.drawable.add_image),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "Image Post")
        }

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight()
                .clickable {

                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = R.drawable.write),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "Text Post")
        }

    }
}

@ExperimentalCoilApi
@Composable
fun HomeFeedContent(
    posts: List<PostResponse>,
    loadingMore: Boolean = true,
    navController: NavHostController,
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        TopFeedBar(
            onImagePostClicked = {
                navController.navigate(HomeNavigationRouting.AddPost) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onTextPostClicked = {

            }
        )

//        OutlinedTextField(
//            value = "",
//            onValueChange = {},
//            placeholder = {
//                Text(text = "write what you want")
//            },
//            modifier = Modifier
//                .background(Color.White)
//                .padding(2.dp)
//                .fillMaxWidth()
//                .clickable {
//                    navController.navigate(HomeNavigationRouting.AddPost) {
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black,
//            ),
//            enabled = false,
//        )

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
