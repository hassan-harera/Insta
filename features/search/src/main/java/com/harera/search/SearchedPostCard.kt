package com.harera.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.harera.base.base.LocalStoreViewModel
import com.harera.base.coil.CoilLoader
import com.harera.base.theme.InstaTheme
import com.harera.base.theme.captionSize
import com.harera.base.theme.timeSize
import com.harera.model.response.SearchedPost
import com.harera.time.TimeUtils.Companion.timeFromNow
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel


@Composable
fun SearchedPostCard(
    searchedPost: SearchedPost,
    onProfileClicked: (String) -> Unit,
    onPostClicked: (Int) -> Unit,
) {

    InstaTheme {
        PostCardContent(
            onProfileClicked = onProfileClicked,
            onPostClicked = onPostClicked,
            searchedPost = searchedPost,
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun PostCardContent(
    searchedPost: SearchedPost,
    onProfileClicked: (String) -> Unit,
    onPostClicked: (Int) -> Unit,
    localStoreViewModel: LocalStoreViewModel = getViewModel(),
    coilLoader: CoilLoader = get(),
) {
    val context = LocalContext.current
    var dropDownState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onPostClicked(searchedPost.postId)
            },
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = {
                        dropDownState = true
                    },
                ) {
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                            }
                            .padding(6.dp),
                        tint = MaterialTheme.colors.secondary,
                    )
                }

                DropdownMenu(
                    expanded = dropDownState,
                    onDismissRequest = {
                        dropDownState = false
                    },
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Text(
                        text = "Delete",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Image(
                        painter = rememberImagePainter(coilLoader.imageRequest(searchedPost.userImageUrl)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                onProfileClicked(searchedPost.username)
                            }
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Column {
                        Text(
                            //TODO change text value
                            text = searchedPost.profileName,
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontStyle = FontStyle.Normal,
                            ),
                            fontWeight = FontWeight.Bold,
                            maxLines = 4,
                            color = MaterialTheme.colors.primary
                        )


                        Text(
                            text = timeFromNow(searchedPost.time),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = timeSize,
                                fontStyle = FontStyle.Normal,
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = searchedPost.caption,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = captionSize,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(5.dp))

                Image(
                    painter = rememberImagePainter(
                        request = ImageRequest.Builder(context)
                            .data(searchedPost.postImageUrl)
                            .crossfade(true)
                            .dispatcher(Dispatchers.IO)
                            .addHeader(HttpHeaders.Authorization,
                                "Bearer ${localStoreViewModel.token}")
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    alpha = 1.0f,
                    alignment = Alignment.Center
                )
            }
        }
    }
}