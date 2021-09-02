package com.whiteside.insta.ui.profile.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.whiteside.insta.modelget.Profile
import com.whiteside.insta.ui.components.PostListView
import com.whiteside.insta.ui.data.DummyDate
import com.whiteside.insta.ui.theme.Grey660
import com.whiteside.insta.ui.viewpost.PostViewModel
import kotlinx.coroutines.launch


@ExperimentalCoilApi
@Composable
fun HomeProfile(
    homeProfileViewModel: HomeProfileViewModel,
    postViewModel: PostViewModel,
    navController: NavHostController,
) {
    homeProfileViewModel.getPosts()
    homeProfileViewModel.getProfile()

    val scrollState = rememberScrollState()

    HomeProfileContent(
        scrollState,
        postViewModel,
        navController,
        homeProfileViewModel
    )
}

@ExperimentalCoilApi
@Composable
fun HomeProfileContent(
    scrollState: ScrollState,
    postViewModel: PostViewModel,
    navController: NavHostController,
    homeProfileViewModel: HomeProfileViewModel,
) {
    val profile by homeProfileViewModel.profile
    val posts by homeProfileViewModel.posts
    val scope = rememberCoroutineScope()

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
                posts,
                navController = navController,
                postViewModel = postViewModel
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

@Preview
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
            .padding(top = 10.dp, start = 10.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = profile.profileImageUrl),
            contentDescription = null,
            Modifier
                .fillMaxHeight()
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                //TODO change text value
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
                //TODO change text value
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
