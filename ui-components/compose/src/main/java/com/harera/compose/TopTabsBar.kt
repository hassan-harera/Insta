package com.harera.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.harera.base.navigation.home.HomeBottomNavigation
import com.harera.base.theme.White
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun Tabs(tabs: List<HomeBottomNavigation>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = White,
        contentColor = Color.Unspecified,
    ) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                selectedContentColor = Color.Unspecified,
                unselectedContentColor = Color.Unspecified,
                icon = {
                    tab.painter
                },
                text = {},
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val tabs = listOf(
        HomeBottomNavigation.Feed,
        HomeBottomNavigation.Profile,
        HomeBottomNavigation.Notifications,
        HomeBottomNavigation.Chats
    )

    val pagerState = rememberPagerState(initialPage = 0)
    Tabs(tabs = tabs, pagerState = pagerState)
}