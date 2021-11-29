package com.harera.posting

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harera.base.navigation.home.HomeNavigationRouting
import com.harera.base.utils.image.ImageUtils
import com.harera.compose.Toast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@Composable
fun PostingNavigation(
    navController: NavHostController,
    postingViewModel: ImagePosting = getViewModel(),
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
            onPostingClicked = { image, caption ->
                scope.launch {
                    postingViewModel.sendIntent(
                        PostingIntent.Post(
                            caption = caption,
                            image = image
                        )
                    )
                }
            },
            onBackClicked = {
                navController.popBackStack()
            }
        )
}

@Preview
@Composable
fun PostFormPreview() {
    PostForm(
        onPostingClicked = { uri: Bitmap, caption: String ->

        }, {

        }
    )
}

@Composable
fun PostForm(
    onPostingClicked: (uri: Bitmap, caption: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var caption by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    val context = LocalContext.current
    val bitmap = ImageUtils.getImageFromUri(imageUri, context = context)

    Scaffold(
        bottomBar = {
            TopAppBar(
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.background,
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClicked()
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
                            onPostingClicked(
                                bitmap!!,
                                caption
                            )
                        },
                        enabled = imageUri != null
                    ) {
                        Text(
                            text = stringResource(id = R.string.post),
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.primary,
                        )
                    }
                },
                elevation = 8.dp
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .verticalScroll(scrollState)
        ) {

            OutlinedTextField(
                value = caption,
                onValueChange = {
                    caption = it
                },
                placeholder = {
                    Text(text = "Caption")
                },
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.primaryVariant,
                    unfocusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                    textColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.secondary,
                ),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )

            PostImage(bitmap) {
                launcher.launch("image/*")
            }
        }
    }
}

@Composable
fun PostImage(
    bitmap: Bitmap?,
    onImageClicked: () -> Unit,
) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable {
                    onImageClicked()
                },
        )
    } else {
        Image(
            painterResource(id = R.drawable.add_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable {
                    onImageClicked()
                },
        )
    }
}
