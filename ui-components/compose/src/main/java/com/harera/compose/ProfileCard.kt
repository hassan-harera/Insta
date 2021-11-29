package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.harera.base.coil.CoilLoader
import com.harera.base.theme.timeSize
import com.harera.model.response.Connection
import org.koin.androidx.compose.get

@Composable
@Preview(showBackground = true)
fun ProfileCardPreview() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Gson()
                .fromJson(
                    "    {\n" +
                            "        \"username\": \"2\",\n" +
                            "        \"profileName\": \"hassan\",\n" +
                            "        \"userImageUrl\": \"http://192.168.1.15:8080/null\"\n" +
                            "    }",
                    Connection::class.java
                ).let {
                    ProfileCard(
                        user = it,
                        onClick = {

                        }
                    )
                }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileCard(
    user: Connection,
    coilLoader: CoilLoader = get(),
    onClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .padding(bottom = 3.dp, top = 3.dp)
            .clickable {
                onClick(user.username)
            },
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = (Alignment.CenterVertically)
        ) {
            Image(
                painter = rememberImagePainter(request = coilLoader.imageRequest(user.userImageUrl)),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
                    .clip(CircleShape),
                alignment = Alignment.CenterStart,
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                Text(
                    //TODO change text value
                    text = user.profileName,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.primary,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )

                Text(
                    text = "username " + user.username,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = timeSize,
                        color = MaterialTheme.colors.primary,
                        fontStyle = FontStyle.Italic,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

