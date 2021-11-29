package com.harera.mychats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource


@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = Modifier.fillMaxWidth(0.15f),
        onClick = {
            onClick()
        },
        shape = CircleShape,
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary,
        )
    }
}