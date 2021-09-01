package com.whiteside.insta.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.whiteside.insta.modelget.Post
import com.whiteside.insta.ui.home.HomeNavigation
import com.whiteside.insta.ui.viewpost.PostViewModel

@ExperimentalCoilApi
@Composable
fun PostListView(
    posts: List<Post>,
    navController: NavHostController,
    postViewModel: PostViewModel
) {
    Column {
        posts.forEach { post ->
            PostCard(
                post = post,
                onPostClicked = {
                    navController.navigate("${HomeNavigation.PostScreen}/${post.postId}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onProfileClicked = {
                    navController.navigate("${HomeNavigation.VisitProfile}/${post.uid}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLikeClicked = {
                    postViewModel.likeClicked(post.postId)
                },
                onCommentSubmitted = { comment ->
                    postViewModel.addComment(
                        comment = comment,
                        postId = post.postId
                    )
                }
            )
        }
    }
}