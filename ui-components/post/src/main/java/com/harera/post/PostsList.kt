package com.harera.post

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.model.model.Post
import com.harera.base.navigation.home.HomeNavigation


@ExperimentalCoilApi
@Composable
fun PostListView(
    posts: List<PostDetails>,
    navController: NavHostController,
) {
    Column {
        val postsCopies = posts.toList()
        postsCopies.forEach{ post ->
            PostCard(
                post = post,
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
    navController.navigate("${HomeNavigation.VisitProfile}/${uid}") {
        launchSingleTop = true
        restoreState = true
    }
}

fun viewPost(navController: NavHostController, postId: String) {
    navController.navigate("${HomeNavigation.PostScreen}/${postId}") {
        launchSingleTop = true
        restoreState = true
    }
}
