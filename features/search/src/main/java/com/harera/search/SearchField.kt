package com.harera.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Preview
private fun SearchFieldPreview() {
    SearchField {

    }
}

@ExperimentalComposeUiApi
@Composable
internal fun SearchField(
    onSearchSubmitted: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchSubmitted(text)
            }
        ),
        placeholder = {
            Text(text = "Search")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        textStyle = TextStyle(
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Start
        ),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colors.secondary,
            focusedLabelColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.primary,
            trailingIconColor = MaterialTheme.colors.background,
            unfocusedIndicatorColor = MaterialTheme.colors.background,
            focusedIndicatorColor = MaterialTheme.colors.background,
            unfocusedLabelColor = MaterialTheme.colors.background,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    )
}
