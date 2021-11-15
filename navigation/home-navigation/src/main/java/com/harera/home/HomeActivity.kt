package com.harera.home

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            HomeActivityContent()
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
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberCoroutineScope()
        var expanded by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                HomeTopBar(
                    onMessagesClicked = {
                        expanded = false
                    },
                    onSearchClicked = {
                        expanded = true
                    }
                )
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
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
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
            HomeProfile(navController = navController)
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
        backgroundColor = Color.Black,
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


@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    HomeTopBar(
        {}, {}
    )
}

@Composable
private fun HomeTopBar(
    onMessagesClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentPadding = PaddingValues(5.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "Insta",
                color = Orange158,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterStart),
            )

            Row(
                Modifier
                    .align(alignment = Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = Orange158,
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(5.dp)
                        .clickable {
                            onSearchClicked()
                        },
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    tint = Orange158,
                    painter = painterResource(id = R.drawable.ic_message_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(5.dp)
                        .clickable {
                            onMessagesClicked()
                        },
                )
            }
        }
    }
}


@Composable
private fun TopBar(
    searchField: Boolean,
    title: @Composable () -> Unit = {
        Text(text = "Insta")
    },
    navigation: @Composable () -> Unit = {
        IconButton(
            onClick = {

            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = ""
            )
        }
    },
    setNavigationButton: Boolean,
    actions: @Composable (RowScope) -> Unit = { Row {} },
    searchWord: MutableState<String> = mutableStateOf(""),
) {
    var searchWordText by remember { searchWord }

    TopAppBar(
        elevation = 5.dp,
        modifier = Modifier.padding(4.dp),
        backgroundColor = Color.White,
        contentColor = Orange158,
    ) {
        HomeTopBarContent()
    }
}

@Composable
@Preview(showBackground = true)
fun HomeTopBarContent() {
    TopAppBar(
        elevation = 5.dp,
        modifier = Modifier.padding(4.dp),
        backgroundColor = Color.White,
        contentColor = Orange158,
    ) {}
}

@Composable
@Preview
private fun SearchFieldPreview() {
    SearchField {

    }
}

@Composable
private fun SearchField(
    onSearchSubmitted: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        label = {
            Text(
                text = "Search",
                textAlign = TextAlign.End
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            textColor = Color.White,
            trailingIconColor = Color.Black,
            backgroundColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedLabelColor = Color.Unspecified,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSearchSubmitted(text)
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        onValueChange = {
            text = it
        },
        singleLine = true,
    )
}


@ExperimentalMaterialApi
@Composable
private fun TopSheet(expanded: Boolean) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            if (expanded)
                BottomSheetValue.Expanded
            else
                BottomSheetValue.Collapsed
        )
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            SearchField {

            }
        },
        sheetPeekHeight = 0.dp,
    ) {}

}