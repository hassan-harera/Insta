package com.harera.home

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
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
import com.harera.base.navigation.home.HomeBottomNavigation
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.navigation.home.HomeNavigationRouting.AddPost
import com.harera.base.navigation.home.HomeNavigationRouting.HomeChats
import com.harera.base.navigation.home.HomeNavigationRouting.HomeNotifications
import com.harera.base.navigation.home.HomeNavigationRouting.PostScreen
import com.harera.base.navigation.home.HomeNavigationRouting.VisitProfile
import com.harera.base.theme.Orange158
import com.harera.chat_navigaton.HomeChats
import com.harera.compose.HomeTopBar
import com.harera.feed.HomeFeed
import com.harera.notifications.HomeNotifications
import com.harera.posting.PostingNavigation
import com.harera.profile.HomeProfile
import com.harera.psot.PostScreen
import com.harera.repository.Constants.POST_ID
import com.harera.repository.Constants.UID
import com.harera.visit_profile.VisitProfile


@ExperimentalCoilApi
@ExperimentalComposeUiApi
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            HomeActivityContent()
        }
    }

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
                if (currentRoute?.destination?.route != HomeChats) {
                    HomeTopBar()
                }
            },
            bottomBar = {
                if (currentRoute?.destination?.route != HomeChats) {
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

    @Composable
    private fun HomeNavHost(navController: NavHostController, paddingValues: PaddingValues) {
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
                HomeProfile(
                    navController = navController
                )
            }
            composable(HomeNotifications) {
                HomeNotifications()
            }
            composable(HomeChats) {
                HomeChats()
            }
            composable("$VisitProfile/{$UID}") { stack ->
                VisitProfile(
                    uid = stack.arguments!!.getString(UID)!!,
                    navController = navController,
                )
            }
            composable("$PostScreen/{$POST_ID}") { stack ->
                PostScreen(
                    postId = stack.arguments!!.getString(POST_ID)!!.toInt(),
                    navController = navController
                )
            }
            composable(AddPost) { stack ->
                PostingNavigation(
                    navController,
                )
            }
        }
    }

    @Composable
    fun HomeBottomNavigation(
        tabs: List<HomeBottomNavigation>,
        navController: NavHostController,
    ) {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = Orange158,
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
