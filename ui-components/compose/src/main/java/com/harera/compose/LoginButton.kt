package com.harera.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Grey660


@Composable
@Preview
fun ButtonToLoginPreview() {
    ButtonToLogin(true) {
    }
}


@Composable
fun ButtonToLogin(
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        enabled = isEnabled,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(Grey660),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(text = "Login")
    }
}