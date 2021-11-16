package com.harera.post

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.navigation.home.HomeNavigationRouting.PostScreen
import com.harera.model.response.PostResponse
import com.harera.post.PostCard as PostCard


@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun PostListView(
    posts: List<PostResponse>,
    navController: NavHostController,
) {
    Column {
        posts.toList().forEach { post ->
            PostCard(
                postResponse = post,
                onPostClicked = { postId ->
                    viewPost(navController, postId)
                },
                onProfileClicked = { uid ->
                    viewProfile(navController, uid)
                },
            )
        }
    }
}

fun viewProfile(navController: NavHostController, uid: String) {
    navController.navigate("${HomeNavigationRouting.VisitProfile}/${uid}") {
        launchSingleTop = true
        restoreState = true
    }
}

fun viewPost(navController: NavHostController, postId: Int) {
    navController.navigate("${PostScreen}/$postId") {
        launchSingleTop = true
        restoreState = true
    }
}
