package com.harera.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.harera.base.theme.Grey660
import com.harera.base.navigation.home.HomeBottomNavigation


@Composable
fun HomeBottomNavigation(
    tabs: List<HomeBottomNavigation>,
    navController: NavHostController
) {
    androidx.compose.material.BottomNavigation(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = Grey660,
        contentColor = Color.Unspecified,
        elevation = 0.dp,

        ) {
        tabs.forEachIndexed { index, tab ->
            IconButton(
                onClick = {
                    navController.navigate(tabs[index].route)
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

@Composable
@Preview
private fun BottomNavigationPreview() {
    val tabs = listOf(
        HomeBottomNavigation.Feed,
        HomeBottomNavigation.Profile,
        HomeBottomNavigation.Notifications,
        HomeBottomNavigation.Chats
    )

    val navController = rememberNavController()
    HomeBottomNavigation(tabs = tabs, navController = navController)
}
