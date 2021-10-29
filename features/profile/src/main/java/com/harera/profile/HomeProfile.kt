package com.harera.profile

import android.util.Log
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
import com.harera.base.theme.Grey200
import com.harera.base.theme.Grey660
import com.harera.compose.Toast
import com.harera.model.model.Post
import com.harera.model.model.Profile
import com.harera.post.PostListView
import com.harera.repository.data.DummyDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val TAG = "HomeProfile"

@ExperimentalCoilApi
@Composable
fun HomeProfile(
    homeProfileViewModel: HomeProfileViewModel = getViewModel(),
    navController: NavHostController,
) {
    var profile by remember { mutableStateOf<Profile?>(null) }
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }

    LaunchedEffect(true) {
        homeProfileViewModel.intent.send(ProfileIntent.GetPosts)
        homeProfileViewModel.intent.send(ProfileIntent.GetProfile)
    }

    val state = homeProfileViewModel.state
    when (state) {
        is ProfileState.Loading -> {
            Shimmer()
        }

        is ProfileState.Error -> {
            Toast(message = state.message)
        }

        is ProfileState.PostsFetched -> {
            posts = state.postList
            HomeProfileContent(
                profile,
                posts,
                navController,
            )
        }

        is ProfileState.ProfilePrepared -> {
            profile = state.profile
            HomeProfileContent(
                profile,
                posts,
                navController,
            )
        }
    }

    Log.d(TAG, "Profile: ${profile.toString()}")
    Log.d(TAG, "posts: ${posts.toMutableList()}")
    Log.d(TAG, "State: ${state::class.java.name}")
}

@Composable
fun Shimmer() {
    LoadingProfileListShimmer()
}

@ExperimentalCoilApi
@Composable
fun HomeProfileContent(
    profile: Profile?,
    posts: List<Post>,
    navController: NavHostController,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Log.d("HomeProfileContent: ", profile.toString())
    Log.d("HomeProfileContent: ", posts.size.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
    ) {
        profile?.let {
            ProfileHeader(profile = it)
        }

        Box {
            PostListView(
                navController = navController,
                posts = posts,
            )

            if (scrollState.value > 0) {
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
                        backgroundColor = Grey660,
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = Color.White
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
    ProfileHeader(profile = DummyDate.PROFILE)
}

@ExperimentalCoilApi
@Composable
fun ProfileHeader(profile: Profile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Grey200)
            .padding(top = 10.dp, start = 10.dp),
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            Modifier
                .clip(CircleShape)
                .fillMaxHeight(0.8f)
                .fillMaxWidth(0.3f)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = profile.name,
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
                text = profile.bio,
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
        backgroundColor = Grey660,
        contentColor = Grey660,
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
