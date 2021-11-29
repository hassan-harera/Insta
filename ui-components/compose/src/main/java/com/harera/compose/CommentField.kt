package com.harera.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CommentField() {
    var comment by remember { mutableStateOf("") }

    OutlinedTextField(
        value = comment,
        onValueChange = {
            comment = it
        },
        placeholder = {
            Text(text = "Comment")
        },
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
//                        .height(220.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            autoCorrect = true,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            focusedLabelColor = MaterialTheme.colors.background,
            focusedIndicatorColor = MaterialTheme.colors.background,
            unfocusedLabelColor = MaterialTheme.colors.background,
            unfocusedIndicatorColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.secondary,
        )
    )
}
