package com.harera.post

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.navigation.home.HomeNavigationRouting.PostScreen
import com.harera.model.response.PostResponse


@ExperimentalCoilApi
@Composable
fun PostListView(
    posts: List<PostResponse>,
    navController: NavHostController,
) {
    Column {
        val postsCopies = posts.toList()
        postsCopies.forEach { post ->
            PostCard(
                postDetails = post,
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
