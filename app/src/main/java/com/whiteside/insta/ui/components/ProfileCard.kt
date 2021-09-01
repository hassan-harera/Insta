package com.whiteside.insta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.whiteside.insta.modelget.Profile
import com.whiteside.insta.ui.data.DummyDate
import com.whiteside.insta.ui.theme.Grey660
import com.whiteside.insta.ui.theme.timeSize

@Composable
@Preview
fun ProfileCardPreview() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            ProfileCard(DummyDate.PROFILE, {})
        }
        item {
            ProfileCard(DummyDate.PROFILE, {})
        }
        item {
            ProfileCard(DummyDate.PROFILE, {})
        }
        item {
            ProfileCard(DummyDate.PROFILE, {})
        }
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .clickable {
                onClick(profile.uid)
            },
        elevation = 5.dp,
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = (Alignment.CenterVertically)
        ) {
            Image(
                //TODO replace image painter with link
                painter = rememberImagePainter(profile.profileImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
                    .clip(CircleShape)
//                    .border(3.dp, color = Grey660)
                ,
                alignment = Alignment.CenterStart
            )

            Spacer(modifier = Modifier.size(15.dp))

            Column {
                Text(
                    //TODO change text value
                    text = profile.name,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )

                Text(
                    text = profile.bio,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = timeSize,
                        color = Grey660,
                        fontStyle = FontStyle.Italic,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

