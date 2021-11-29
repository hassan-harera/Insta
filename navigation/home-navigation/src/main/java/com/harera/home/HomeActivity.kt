package com.harera.home

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.navigation.home.HomeBottomNavigation
import com.harera.base.navigation.home.HomeNavigationRouting.HomeChats
import com.harera.base.navigation.home.HomeNavigationRouting.ImagePosting
import com.harera.base.navigation.home.HomeNavigationRouting.TextPosting
import com.harera.base.theme.InstaTheme
import com.harera.chat_navigaton.MessagesService
import com.harera.search.SearchActivity


@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val componentName = startService(Intent(this, MessagesService::class.java))

        setContent {
            InstaTheme {
                HomeActivityContent()
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
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

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val state = rememberCoroutineScope()
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        val showTopBar = currentRoute?.destination?.route?.let {
            (it != ImagePosting && it != HomeChats && it != TextPosting)
        } ?: true

        val showBottomBar = currentRoute?.destination?.route?.let {
            (it != ImagePosting && it != HomeChats && it != TextPosting)
        } ?: true

        Scaffold(
            topBar = {
                if (showTopBar)
                    HomeTopBar(
                        onMessagesClicked = {

                        },
                        onSearchClicked = {
                            context.startActivity(Intent(context, SearchActivity::class.java))
                            expanded = false
                        }
                    )
            },
            bottomBar = {
                if (showBottomBar) {
                    HomeBottomNavigation(
                        tabs = bottomNavIcons,
                    ) {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        ) {
            HomeNavHost(navController = navController, it)
        }
    }
}