package com.harera.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.chat_navigaton.HomeChats
import com.harera.feed.HomeFeed
import com.harera.notifications.HomeNotifications
import com.harera.posting.PostingNavigation
import com.harera.profile.HomeProfile
import com.harera.psot.PostScreen
import com.harera.repository.Constants
import com.harera.text_post.TextPosting
import com.harera.visit_profile.VisitProfile

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun HomeNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        startDestination = HomeNavigationRouting.HomeFeed,
        navController = navController,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(HomeNavigationRouting.HomeFeed) {
            HomeFeed(
                navController = navController,
            )
        }
        composable(HomeNavigationRouting.HomeProfile) {
            HomeProfile(navController = navController)
        }
        composable(HomeNavigationRouting.HomeNotifications) {
            HomeNotifications(navController = navController)
        }
        composable(HomeNavigationRouting.HomeChats) {
            HomeChats(navController = navController)
        }
        composable("${HomeNavigationRouting.VisitProfile}/{${Constants.UID}}") { stack ->
            VisitProfile(
                uid = stack.arguments!!.getString(Constants.UID)!!,
                navController = navController,
            )
        }
        composable("${HomeNavigationRouting.PostScreen}/{${Constants.POST_ID}}") { stack ->
            PostScreen(
                postId = stack.arguments!!.getString(Constants.POST_ID)!!.toInt(),
                navController = navController
            )
        }
        composable(HomeNavigationRouting.ImagePosting) { stack ->
            PostingNavigation(
                navController,
            )
        }
        composable(HomeNavigationRouting.TextPosting) { stack ->
            TextPosting(
                navController,
            )
        }
    }
}
