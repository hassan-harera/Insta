package com.harera.insta

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
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
import com.harera.base.navigation.HomeBottomNavigation
import com.harera.base.navigation.HomeNavigation
import com.harera.base.navigation.HomeNavigation.AddPost
import com.harera.base.navigation.HomeNavigation.PostScreen
import com.harera.base.navigation.HomeNavigation.VisitProfile
import com.harera.base.theme.Grey660
import com.harera.chat.HomeChats
import com.harera.compose.HomeTopBar
import com.harera.feed.HomeFeed
import com.harera.insta.ui.viewpost.PostScreen
import com.harera.posting.PostForm
import com.harera.profile.HomeProfile
import com.harera.repository.common.Constansts.POST_ID
import com.harera.repository.common.Constansts.UID
import com.harera.visit_profile.VisitProfile


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
class HomeActivity : AppCompatActivity() {

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
            HomeBottomNavigation.Feed,
            HomeBottomNavigation.Profile,
            HomeBottomNavigation.Notifications,
            HomeBottomNavigation.Chats
        )

        val navController = rememberNavController()
        val currentRoute by navController.currentBackStackEntryAsState()

        Scaffold(
            topBar = {
                if (currentRoute?.destination?.route != HomeNavigation.HomeChats) {
                    HomeTopBar()
                }
            },
            bottomBar = {
                if (currentRoute?.destination?.route != HomeNavigation.HomeChats) {
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
            startDestination = HomeNavigation.HomeFeed,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(HomeNavigation.HomeFeed) {
                HomeFeed(
                    navController = navController,
                )
            }
            composable(HomeNavigation.HomeProfile) {
                HomeProfile(
                    navController = navController
                )
            }
            composable(HomeNavigation.HomeNotifications) {
                com.harera.notifications.HomeNotifications()
            }
            composable(HomeNavigation.HomeChats) {
                HomeChats()
            }
            composable("$VisitProfile/{$UID}") { stack ->
                VisitProfile(
                    navController = navController,
                    uid = stack.arguments!!.getString(UID)!!
                )
            }
            composable("$PostScreen/{$POST_ID}") { stack ->
                PostScreen(
                    postId = stack.arguments!!.getString(POST_ID)!!,
                    navController = navController
                )
            }
            composable(AddPost) { stack ->
                PostForm(
                    navController,
                )
            }
        }
    }

    @Composable
    fun HomeBottomNavigation(
        tabs: List<HomeBottomNavigation>,
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
