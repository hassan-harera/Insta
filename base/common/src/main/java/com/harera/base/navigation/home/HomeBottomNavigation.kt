package com.harera.base.navigation.home

import com.harera.base.R

sealed class HomeBottomNavigation(var icon: Int, var route: String) {
    object Feed : HomeBottomNavigation(
        R.drawable.home,
        HomeNavigationRouting.HomeFeed
    )

    object Profile : HomeBottomNavigation(
        R.drawable.profile,
        HomeNavigationRouting.HomeProfile
    )

    object Notifications :
        HomeBottomNavigation(
            R.drawable.notification,
            HomeNavigationRouting.HomeNotifications
        )

    object Chats : HomeBottomNavigation(
        R.drawable.chats,
        HomeNavigationRouting.HomeChats
    )
}