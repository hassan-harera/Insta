package com.harera.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.navigation.home.HomeBottomNavigation
import com.harera.base.theme.InstaTheme

@Composable
@Preview(showBackground = true)
fun HomeBottomNavigationPreview() {
    InstaTheme {
        HomeBottomNavigation(
            arrayListOf(
                HomeBottomNavigation.Feed,
                HomeBottomNavigation.Profile,
                HomeBottomNavigation.Notifications,
                HomeBottomNavigation.Chats,
            )
        ) {

        }
    }
}

@Composable
fun HomeBottomNavigation(
    tabs: List<HomeBottomNavigation>,
    onIconClicked: (String) -> Unit,
) {
    var currentIdx by remember { mutableStateOf(0) }

    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
    ) {
        tabs.forEachIndexed { index, tab ->
            IconButton(
                onClick = {
                    currentIdx = index
                    onIconClicked(tabs[index].route)
                },
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.Unspecified),
            ) {
                tab.painter?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        tint = if (index == currentIdx)
                            MaterialTheme.colors.secondary
                        else
                            MaterialTheme.colors.primary
                    )
                }

                tab.imageVector?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (index == currentIdx)
                            MaterialTheme.colors.secondary
                        else
                            MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}
