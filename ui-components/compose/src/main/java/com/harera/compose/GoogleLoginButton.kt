package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Shapes

@Composable
@Preview
fun GoogleRegisterButtonPreview() {
    GoogleRegisterButton {

    }
}

@Composable
fun GoogleRegisterButton(
    onClickAction: () -> Unit,
) {
    Button(
        onClick = onClickAction,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 2.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        shape = Shapes.large
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "",
        )
    }
}