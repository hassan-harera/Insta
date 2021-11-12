package com.harera.posting

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
import androidx.compose.ui.graphics.Color
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
import com.harera.base.theme.Orange158
import com.harera.base.utils.image.ImageUtils
import com.harera.compose.Toast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@Composable
fun PostingNavigation(
    navController: NavHostController,
    postingViewModel: PostingViewModel = getViewModel(),
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
            onPostingClicked = { uri, caption ->
                scope.launch {
                    postingViewModel.sendIntent(
                        PostingIntent.Post(
                            caption = caption,
                            imageUri = uri
                        )
                    )
                }
            },
        )
}

@Preview
@Composable
fun PostFormPreview() {
    PostForm(
        onPostingClicked = { uri: Uri, caption: String ->

        }
    )
}

@Composable
fun PostForm(
    onPostingClicked: (uri: Uri, caption: String) -> Unit,
) {
    val scrollState = rememberScrollState()
    var caption by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Scaffold(
        bottomBar = {
            TopAppBar(
                contentColor = Orange158,
                backgroundColor = Orange158,
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {},
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
                            onPostingClicked(imageUri!!, caption)
                        },
                        enabled = imageUri != null
                    ) {
                        Text(
                            text = stringResource(id = R.string.post),
                            fontSize = 18.sp,
                        )
                    }
                },
                elevation = 0.dp
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
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
                    .background(Color.White)
                    .height(220.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                ),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )

            PostImage(imageUri) {
                launcher.launch("image/*")
            }
        }
    }
}

@Composable
fun PostImage(
    uri: Uri?,
    onImageClicked: () -> Unit,
) {
    val context = LocalContext.current
    val bitmap = ImageUtils.getImageFromUri(uri, context = context)

    if (uri != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
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
