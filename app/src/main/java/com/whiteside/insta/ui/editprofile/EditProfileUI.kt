package com.whiteside.insta.ui.editprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.whiteside.insta.R
import com.whiteside.insta.ui.components.TopBar
import com.whiteside.insta.ui.theme.Grey660
import com.whiteside.insta.ui.theme.Grey700


@Preview
@Composable
fun EditProfileView() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                searchField = false,
                setNavigationButton = true,
                title = {
                    Text(text = "Profile")
                }
            )
        }
    ) {
        EditProfileForm()
    }
}

@Preview
@Composable
fun EditProfileForm() {
    Column(
        Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        var name by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }
        var dateClicked by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.15f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )

            FloatingActionButton(
                onClick = {},
                backgroundColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
//                    .fillMaxSize(0.2f)
            ) {
                Icon(
//                    modifier =
//                    Modifier
//                        .fillMaxSize(0.8f),
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            //TODO change text value
            onValueChange = {
                name = it
            },
            value = name,
            placeholder = {
                Text(text = "Name...")
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Grey660,
                focusedLabelColor = Grey660,
                textColor = Color.Black,
                backgroundColor = Color.Unspecified,
                focusedIndicatorColor = Grey660
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            //TODO change text value
            onValueChange = {
                bio = it
            },
            value = bio,
            placeholder = {
                Text(text = "Bio...")
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Grey660,
                focusedLabelColor = Grey660,
                textColor = Color.Black,
                backgroundColor = Color.Unspecified,
                focusedIndicatorColor = Grey660
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                dateClicked = true
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = "Date of birth", color = Grey700)
        }

        val dialog = remember { MaterialDialog() }
        dialog.build(buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }) {
            datepicker { date ->

            }
        }

//        if (dateClicked)
        dialog.build(
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            },
        ) {
            datepicker { date ->

            }
        }
    }

}
