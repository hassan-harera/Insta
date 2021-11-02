package com.harera.visit_profile

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.harera.base.theme.Grey660
import com.harera.compose.Toast
import com.harera.compose.VisitProfileHeader
import com.harera.model.model.Post
import com.harera.model.model.Profile
import com.harera.post.PostDetails
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
    var profile by remember { mutableStateOf<Profile?>(null) }
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }

    LaunchedEffect(true) {
        visitProfileViewModel.setUid(uid = uid)
        visitProfileViewModel.intent.send(ProfileIntent.GetPosts)
        visitProfileViewModel.intent.send(ProfileIntent.GetProfile)
    }

    val state = visitProfileViewModel.state
    when (state) {
        is ProfileState.Loading -> {
        }

        is ProfileState.Error -> {
            Toast(message = state.message)
        }

        is ProfileState.PostsFetched -> {
            posts = state.postList
            VisitProfileContent(
                profile,
                posts,
                navController,
            ) {
                TODO()
            }
        }

        is ProfileState.ProfilePrepared -> {
            profile = state.profile
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

@ExperimentalCoilApi
@Composable
fun VisitProfileContent(
    profile: Profile?,
    posts: List<PostDetails>,
    navController: NavHostController,
    onFollowClicked: (String) -> Unit,
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
            VisitProfileHeader(
                profile = profile,
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