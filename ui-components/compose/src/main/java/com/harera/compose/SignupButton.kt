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
import com.harera.base.theme.White

@Composable
@Preview
fun ButtonToRegisterPreview() {
    ButtonToRegister(true) {

    }
}

@Composable
fun ButtonToRegister(isEnabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(text = "Register")
    }
}

