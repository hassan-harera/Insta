package com.harera.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun EmailTextFieldPreview() {
    EmailTextField(
        remember {
            mutableStateOf("")
        }
    ) {

    }
}

@Composable
fun EmailTextField(
    email: MutableState<String>,
    onValueChange: (String) -> Unit,
) {

    OutlinedTextField(
        value = email.value,
        onValueChange = { email.value = it },
        label = { Text(text = "Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next),
    )

}
