package com.harera.text_post

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.compose.Toast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get


@Composable
fun TextPosting(
    navController: NavHostController,
    postingViewModel: TextPostingViewModel = get(),
) {
    val scope = rememberCoroutineScope()
    val state = postingViewModel.state
    var loading by remember { mutableStateOf(value = false) }

    when (state) {
        is PostingState.Error -> {
            Toast(message = state.message)
        }

        is PostingState.Loading -> {
            loading = true
        }

        is PostingState.PostingCompleted -> {
            navController.navigate(
                "${HomeNavigationRouting.PostScreen}/${state.postId}"
            ) {
                launchSingleTop = true
                restoreState = false
            }
        }
    }

    if (loading)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    else
        PostForm(
            onPostingClicked = { color, caption ->
                scope.launch {
                    postingViewModel.sendIntent(PostingIntent.Post(
                        caption = caption,
                        Bitmap.createBitmap(
                            1024,
                            1024,
                            Bitmap.Config.ARGB_8888
                        ).apply {
                            eraseColor(color.toArgb())
                        }
                    ))
                }
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
}


@Composable
private fun PostForm(
    onPostingClicked: (color: Color, caption: String) -> Unit,
    onBackClick: () -> Unit,
) {
    var caption by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Colors.colors[0]) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomTextPostScreen(
                color = color,
                onPostClicked = {
                    onPostingClicked(color, caption)
                },
                onBackClick = onBackClick
            )
        },
    ) {
        PostFormContent(
            color = color,
            caption = caption,
            onCaptionChange = {
                caption = it
            },
            onColorChange = {
                color = it
            },
        )
    }
}

@Composable
fun BottomTextPostScreen(
    onBackClick: () -> Unit,
    onPostClicked: () -> Unit,
    color: Color,
) {
    TopAppBar(
        contentColor = color,
        backgroundColor = MaterialTheme.colors.background,
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_up)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onPostClicked()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.post),
                    fontSize = 18.sp,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        elevation = 4.dp
    )
}

@Composable
fun PostFormContent(
    color: Color,
    caption: String,
    onColorChange: (Color) -> Unit,
    onCaptionChange: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
                .background(color),
        ) {
            OutlinedTextField(
                value = caption,
                onValueChange = {
                    onCaptionChange(it)
                },
                placeholder = {
                    Text(
                        text = "Type..",
                        fontStyle = FontStyle.Normal,
                        fontSize = 40.sp,
                    )
                },
                modifier = Modifier
                    .background(Color.Unspecified)
                    .align(Alignment.Center),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Unspecified,
                    unfocusedBorderColor = Color.Unspecified,
                    focusedLabelColor = Color.Unspecified,
                    unfocusedLabelColor = Color.Unspecified,
                    cursorColor = Color.Black,
                ),
                maxLines = 12,
                textStyle = TextStyle(fontSize = 36.sp),
            )
        }

//        Spacer(modifier = Modifier.fillMaxHeight(0.02f))

        LazyRow {
            Colors.colors.forEachIndexed { index, color ->
                item {
                    Icon(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = null,
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth(0.22f)
                            .background(color)
                            .padding(14.dp)
                            .clickable {
                                onColorChange(color)
                            }
                    )
                }
            }
        }
    }
}
