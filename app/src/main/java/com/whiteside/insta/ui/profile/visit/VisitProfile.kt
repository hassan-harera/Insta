package com.whiteside.insta.ui.profile.visit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.whiteside.insta.modelget.Profile
import com.whiteside.insta.ui.components.PostListView
import com.whiteside.insta.ui.theme.Grey660
import com.whiteside.insta.ui.viewpost.PostViewModel
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun VisitProfile(
    visitProfileViewModel: VisitProfileViewModel,
    postViewModel: PostViewModel,
    navController: NavHostController,
    uid: String
) {
    val profile by visitProfileViewModel.profile.observeAsState()
    val posts by visitProfileViewModel.posts.observeAsState(emptyList())

    visitProfileViewModel.setUid(uid)
    visitProfileViewModel.getProfile()
    visitProfileViewModel.getPosts()

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
    ) {
        profile?.let {
            ProfileHeader(it, visitProfileViewModel, {})
        }

        Box {
            PostListView(
                posts = posts,
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

@ExperimentalCoilApi
@Composable
fun ProfileHeader(
    profile: Profile,
    visitProfileViewModel: VisitProfileViewModel,
    onFollowClicked: (String) -> Unit
) {
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
                .clip(CircleShape)
                .fillMaxHeight()
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

            Spacer(modifier = Modifier.size(15.dp))

            visitProfileViewModel.getFollowButtonState()
            val followButtonState by visitProfileViewModel.followButtonState.observeAsState(false)

            if (followButtonState) {
                TextButton(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Grey660,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "Follow", color = Color.White)
                }
            }
        }
    }
}