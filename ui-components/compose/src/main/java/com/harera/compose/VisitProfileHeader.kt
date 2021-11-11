package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.harera.base.theme.Grey660
import com.harera.model.model.User


@ExperimentalCoilApi
@Composable
fun VisitProfileHeader(
    user: User,
    onFollowClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp, start = 10.dp)
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            Modifier
                .clip(CircleShape)
                .fillMaxHeight(0.8f)
                .fillMaxWidth(0.3f)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = user.name,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = user.bio ?: "",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(15.dp))

            val followButtonState by remember { mutableStateOf(false) }

            if (followButtonState) {
                TextButton(
                    onClick = {
                        onFollowClicked(user.username)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Grey660,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "Follow", color = Color.White)
                }
            }
        }
    }
}