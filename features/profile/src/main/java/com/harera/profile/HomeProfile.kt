package com.harera.profile

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.harera.base.DummyDate
import com.harera.base.base.LocalStoreViewModel
import com.harera.base.coil.CoilLoader
import com.harera.base.coil.CoilUtils.createRequest
import com.harera.base.state.BaseState
import com.harera.base.theme.InstaTheme
import com.harera.base.theme.White
import com.harera.compose.LoadingPostListShimmer
import com.harera.compose.Toast
import com.harera.model.model.User
import com.harera.model.response.PostResponse
import com.harera.post.PostListView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

private const val TAG = "HomeProfile"

@ExperimentalCoilApi
@Composable
fun HomeProfile(
    homeProfileViewModel: HomeProfileViewModel = getViewModel(),
    navController: NavHostController,
) {
    var profile by remember { mutableStateOf<User?>(null) }
    var loading by remember { mutableStateOf<Boolean>(true) }
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }

    LaunchedEffect(true) {
        homeProfileViewModel.intent.send(HomeProfileIntent.GetProfile)
        delay(600)
        homeProfileViewModel.intent.send(HomeProfileIntent.GetPosts)
    }

    val state = homeProfileViewModel.state

    when (state) {
        is BaseState.Loading -> {
            loading = true
            Log.d(TAG, "HomeProfile: $state")
        }

        is BaseState.Error -> {
            loading = false
            Toast(message = state.data.toString())
            Log.d(TAG, "HomeProfile: $state")
        }

        is HomeProfileState.PostsFetched -> {
            loading = false
            posts = state.postList
            Log.d(TAG, "HomeProfile: $state")
        }

        is HomeProfileState.ProfilePrepared -> {
            loading = false
            profile = state.user
            Log.d(TAG, "HomeProfile: $state")
        }
    }

    if (loading)
        Shimmer()
    else
        HomeProfileContent(
            profile,
            posts.sortedByDescending { it.post.time },
            navController,
        )
}

@Composable
fun Shimmer() {
    InstaTheme {
        LoadingPostListShimmer(
            imageHeight = 300.dp,
            padding = 5.dp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoilApi
@Composable
fun HomeProfileContent(
    user: User?,
    posts: List<PostResponse>,
    navController: NavHostController,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Log.d("HomeProfileContent: ", user.toString())
    Log.d("HomeProfileContent: ", posts.size.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        user?.let {
            ProfileHeader(user = it)
        }

        Box {
            PostListView(
                navController = navController,
                posts = posts,
            )

//            TODO handle pop to up floating action button
            if (scrollState.isScrollInProgress) {
                Box(
                    modifier = Modifier
                        .align(BottomEnd)
                        .padding(end = 10.dp, bottom = 10.dp),
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                scrollState.scrollTo(0)
                            }
                        },
                        backgroundColor = MaterialTheme.colors.background,
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader(user = DummyDate.USER)
}

@ExperimentalCoilApi
@Composable
fun ProfileHeader(
    user: User,
    localStoreViewModel: LocalStoreViewModel = getViewModel(),
    coilLoader: CoilLoader = get(),
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(MaterialTheme.colors.secondaryVariant)
            .padding(top = 10.dp, start = 10.dp),
    ) {
        Image(
            painter = rememberImagePainter(coilLoader.imageRequest(user.userImageUrl)),
            contentDescription = null,
            Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth(0.3f)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = user.name,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = user.bio ?: "",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ProfileTopBar() {
    var searchWord by remember { mutableStateOf("") }

    TopAppBar(
        modifier = Modifier.padding(0.dp),
        backgroundColor = White,
        contentColor = White,
        title = {
            TextField(
                value = searchWord,
                onValueChange = {
                    searchWord = it
                },
                label = {
                    Text(
                        text = "Search Any Thing",
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
                    backgroundColor = Color.Unspecified
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        //TODO call search method
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
    )
}
