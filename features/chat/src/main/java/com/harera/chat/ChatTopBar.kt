package com.harera.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.harera.base.DummyDate
import com.harera.base.coil.CoilLoader
import com.harera.model.model.User
import org.koin.androidx.compose.get


@ExperimentalCoilApi
@Composable
fun ChatTopBar(
    user: User,
    onBackClicked: () -> Unit,
    coilLoader: CoilLoader = get(),
) {
    TopAppBar(
        contentPadding = PaddingValues(4.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp
    ) {

        Icon(
            modifier = Modifier.clickable {
                onBackClicked()
            },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "",
            tint = MaterialTheme.colors.primary,
        )

        Image(
            painter = rememberImagePainter(coilLoader.imageRequest(user.userImageUrl)),
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
                .size(40.dp)
                .clip(CircleShape),
            alignment = Alignment.CenterStart
        )

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            text = user.name,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            maxLines = 1,
            color = MaterialTheme.colors.primary,
        )
    }
}


@ExperimentalCoilApi
@Composable
@Preview
fun ChatTopBarPreview() {
    ChatTopBar(user = DummyDate.USER, {})
}
