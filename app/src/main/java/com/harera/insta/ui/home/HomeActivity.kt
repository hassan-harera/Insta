package com.harera.insta.ui.home

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.harera.base.theme.Grey660
import com.harera.chat.HomeChats
import com.harera.compose.HomeTopBar
import com.harera.insta.chat.ChatViewModel
import com.harera.insta.ui.feed.FeedViewModel
import com.harera.insta.ui.feed.HomeFeed
import com.harera.navigation.HomeNavigation.AddPost
import com.harera.notifications.HomeNotifications
import com.harera.notifications.NotificationsViewModel
import com.harera.insta.ui.postform.PostForm
import com.harera.insta.ui.profile.home.HomeProfile
import com.harera.insta.ui.profile.home.HomeProfileViewModel
import com.harera.insta.ui.profile.visit.VisitProfile
import com.harera.insta.ui.profile.visit.VisitProfileViewModel
import com.harera.insta.ui.utils.Arguments.POST_ID
import com.harera.insta.ui.utils.Arguments.UID
import com.harera.insta.ui.viewpost.PostScreen
import com.harera.insta.ui.viewpost.PostViewModel
import com.harera.navigation.HomeNavigation.PostScreen
import com.harera.navigation.HomeNavigation.VisitProfile
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val feedViewModel: FeedViewModel by viewModels()
    private val homeProfileViewModel: HomeProfileViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val visitProfileViewModel: VisitProfileViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val notificationsViewModel: NotificationsViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            HomeActivityContent()
        }
    }

    @ExperimentalComposeUiApi
    @ExperimentalCoilApi
    @Composable
    private fun HomeActivityContent() {
        val bottomNavIcons = listOf(
            com.harera.navigation.HomeBottomNavigation.Feed,
            com.harera.navigation.HomeBottomNavigation.Profile,
            com.harera.navigation.HomeBottomNavigation.Notifications,
            com.harera.navigation.HomeBottomNavigation.Chats
        )

        val navController = rememberNavController()
        val currentRoute by navController.currentBackStackEntryAsState()

        Scaffold(
            topBar = {
                if (currentRoute?.destination?.route != com.harera.navigation.HomeNavigation.HomeChats) {
                    HomeTopBar()
                }
            },
            bottomBar = {
                if (currentRoute?.destination?.route != com.harera.navigation.HomeNavigation.HomeChats) {
                    HomeBottomNavigation(
                        tabs = bottomNavIcons,
                        navController = navController
                    )
                }
            }
        ) {
            HomeNavHost(navController = navController, it)
        }
    }

    @ExperimentalComposeUiApi
    @ExperimentalCoilApi
    @Composable
    private fun HomeNavHost(navController: NavHostController, paddingValues: PaddingValues) {
        NavHost(
            startDestination = com.harera.navigation.HomeNavigation.HomeFeed,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(com.harera.navigation.HomeNavigation.HomeFeed) {
                HomeFeed(
                    feedViewModel = feedViewModel,
                    navController = navController,
                    postViewModel = postViewModel
                )
            }
            composable(com.harera.navigation.HomeNavigation.HomeProfile) {
                HomeProfile(
                    homeProfileViewModel = homeProfileViewModel,
                    postViewModel = postViewModel,
                    navController = navController
                )
            }
            composable(com.harera.navigation.HomeNavigation.HomeNotifications) {
                com.harera.notifications.HomeNotifications(notificationsViewModel = notificationsViewModel)
            }
            composable(com.harera.navigation.HomeNavigation.HomeChats) {
                HomeChats(chatViewModel = chatViewModel)
            }
            composable("$VisitProfile/{$UID}") { stack ->
                VisitProfile(
                    visitProfileViewModel = visitProfileViewModel,
                    navController = navController,
                    postViewModel = postViewModel,
                    uid = stack.arguments!!.getString(UID)!!
                )
            }
            composable("$PostScreen/{$POST_ID}") { stack ->
                PostScreen(
                    postViewModel,
                    stack.arguments!!.getString(POST_ID)!!,
                    navController
                )
            }
            composable(AddPost) { stack ->
                PostForm(
                    navController,
                    postViewModel
                )
            }
        }
    }

    @Composable
    fun HomeBottomNavigation(
        tabs: List<com.harera.navigation.HomeBottomNavigation>,
        navController: NavHostController
    ) {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = Grey660,
            contentColor = Color.Unspecified,
            elevation = 0.dp,
        ) {
            tabs.forEachIndexed { index, tab ->
                IconButton(
                    onClick = {
                        navController.navigate(tabs[index].route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(12.dp),
                ) {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }
        }
    }
}
