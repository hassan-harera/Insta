package com.harera.visit_profile

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.theme.White
import com.harera.compose.Toast
import com.harera.compose.VisitProfileHeader
import com.harera.model.model.User
import com.harera.model.response.PostResponse
import com.harera.post.PostListView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun VisitProfile(
    visitProfileViewModel: VisitProfileViewModel = getViewModel(),
    uid: String,
    navController: NavHostController,
) {
    var profile by remember { mutableStateOf<User?>(null) }
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }

    LaunchedEffect(true) {
        visitProfileViewModel.setUid(uid = uid)
        visitProfileViewModel.intent.send(ProfileIntent.GetPosts)
        visitProfileViewModel.intent.send(ProfileIntent.GetProfile)
    }

    val state = visitProfileViewModel.state
    when (state) {
        is VisitProfileState.Loading -> {
        }

        is VisitProfileState.Error -> {
            Toast(message = state.message)
        }

        is VisitProfileState.PostsFetched -> {
            posts = state.postList
            VisitProfileContent(
                profile,
                posts,
                navController,
            ) {
                TODO()
            }
        }

        is VisitProfileState.ProfilePrepared -> {
            profile = state.user
            VisitProfileContent(
                profile,
                posts,
                navController,
            ) {
                TODO()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoilApi
@Composable
fun VisitProfileContent(
    user: User?,
    posts: List<PostResponse>,
    navController: NavHostController,
    onFollowClicked: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Log.d("HomeProfileContent: ", user.toString())
    Log.d("HomeProfileContent: ", posts.size.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
    ) {
        user?.let {
            VisitProfileHeader(
                user = user,
                onFollowClicked = onFollowClicked
            )
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
                        backgroundColor = White,
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