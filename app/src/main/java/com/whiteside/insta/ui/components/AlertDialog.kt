package com.whiteside.insta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun AlertDialog() {
    var alertDialogState by remember {
        mutableStateOf(true)
    }

    var quantity by remember {
        mutableStateOf("")
    }

    Column {
        if (alertDialogState) {

            AlertDialog(

                onDismissRequest = {
                    alertDialogState = false
                },

                title = {
                    Text(text = "Change Quantity")
                },

                text = {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = {
                            quantity = it
                        },
                        placeholder = {
                            Text(text = "Comment")
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(8.dp)
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
                        keyboardActions = KeyboardActions(
                            onDone = {
                                alertDialogState = false
                            }
                        )
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            alertDialogState = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },

                dismissButton = {
                    Button(
                        onClick = {
                            alertDialogState = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}
