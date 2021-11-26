package com.harera.base.navigation.home

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.harera.base.R

typealias Icon = @Composable () -> Unit

sealed class HomeBottomNavigation(
    val painter: Int? = null,
    val imageVector: ImageVector? = null,
    var route: String,
) {
    object Feed : HomeBottomNavigation(
        imageVector = Icons.Default.Home,
        route = HomeNavigationRouting.HomeFeed
    )

    object Profile : HomeBottomNavigation(
        imageVector = Icons.Default.AccountCircle,
        route = HomeNavigationRouting.HomeProfile
    )

    object Notifications :
        HomeBottomNavigation(
            imageVector = Icons.Default.Notifications,
            route = HomeNavigationRouting.HomeNotifications
        )

    object Chats : HomeBottomNavigation(
        painter = R.drawable.comment,
        route = HomeNavigationRouting.HomeChats
    )
}