package com.harera.navigation

sealed class HomeBottomNavigation(var icon: Int, var route: String) {
    object Feed : HomeBottomNavigation(
        R.drawable.home,
        HomeNavigation.HomeFeed
    )

    object Profile : HomeBottomNavigation(
        R.drawable.profile,
        HomeNavigation.HomeProfile
    )

    object Notifications :
        HomeBottomNavigation(
            R.drawable.notification,
            HomeNavigation.HomeNotifications
        )

    object Chats : HomeBottomNavigation(
        R.drawable.chats,
        HomeNavigation.HomeChats
    )
}