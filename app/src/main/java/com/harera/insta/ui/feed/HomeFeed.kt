package com.harera.insta.ui.feed

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.insta.R.drawable
import com.harera.insta.ui.components.PostListView
import com.harera.insta.ui.home.HomeNavigation
import com.harera.insta.ui.viewpost.PostViewModel

@ExperimentalCoilApi
@Composable
fun HomeFeed(
    feedViewModel: FeedViewModel,
    navController: NavHostController,
    postViewModel: PostViewModel
) {
    val posts by feedViewModel.posts

    feedViewModel.getFollowings()
    feedViewModel.getPosts()

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
            navController = navController,
            postViewModel = postViewModel
        )

        if (posts.isEmpty()) {
            EmptyPostList()
        }
    }
}

@Composable
fun EmptyPostList() {
    Image(
        painter = painterResource(drawable.empty_list),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}
