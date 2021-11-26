package com.harera.feed

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
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
import com.harera.base.state.BaseState
import com.harera.base.state.FeedState
import com.harera.compose.LoadingPostListShimmer
import com.harera.model.response.PostResponse
import com.harera.post.PostListView
import org.koin.androidx.compose.getViewModel

private const val TAG = "HomeFeed"

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
@Stable
fun HomeFeed(
    feedViewModel: FeedViewModel = getViewModel(),
    navController: NavHostController,
) {
    val state = feedViewModel.state

    LaunchedEffect(key1 = true) {
        feedViewModel.intent.send(FeedIntent.FetchPosts)
    }

    Column {
        TopFeedBar(
            onImagePostClicked = {
                navController.navigate(HomeNavigationRouting.ImagePosting) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onTextPostClicked = {
                navController.navigate(HomeNavigationRouting.TextPosting) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        ViewAdapter(
            state = state,
            navController = navController
        )
    }
}

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
private fun ViewAdapter(
    state: BaseState,
    navController: NavHostController,
) {

    when (state) {
        is BaseState.Error -> {
            Toast.makeText(
                LocalContext.current,
                state.data.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        is FeedState.LoadingMore -> {
            LoadingPostListShimmer(
                imageHeight = 300.dp,
                padding = 5.dp
            )
        }

        is FeedState.Posts -> {
            HomeFeedContent(
                posts = state.posts.sortedByDescending { it.post.time },
                navController = navController
            )
        }

        is BaseState.Loading -> {
            LoadingPostListShimmer(
                imageHeight = 300.dp,
                padding = 5.dp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .horizontalScroll(topBar)
            .height(42.dp)
    ) {
        Row {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .clickable {
                        onImagePostClicked()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.add_image),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Image Post")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .clickable {
                        onTextPostClicked()
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
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HomeFeedContent(
    posts: List<PostResponse>,
    loadingMore: Boolean = true,
    navController: NavHostController,
) {
    val scrollState = rememberScrollState()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            SearchBottomSheet()
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

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
}

@Composable
fun SearchBottomSheet() {
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
            .clickable {},
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            focusedLabelColor = MaterialTheme.colors.background,
            focusedIndicatorColor = MaterialTheme.colors.background,
            unfocusedLabelColor = MaterialTheme.colors.background,
            unfocusedIndicatorColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.secondary,
        ),
        enabled = false,
    )

}
