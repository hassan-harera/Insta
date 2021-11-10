package com.harera.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Grey660

@Composable
fun ShimmerPostCardItem(
    colors: List<Color>,
    xShimmer: Float,
    yShimmer: Float,
    cardHeight: Dp,
    gradientWidth: Float,
    padding: Dp,
) {

    val brush = linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .background(Color.White),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.size(15.dp))

                        Spacer(modifier = Modifier.width(40.dp))

                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Spacer(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .padding(
                            start = 5.dp,
                            end = 5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Spacer(
                        modifier = Modifier
                            .weight(0.5f, true)
                            .fillMaxHeight(),
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(0.5f, true)
                            .fillMaxHeight()
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .border(width = 1.dp, color = Grey660)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                Spacer(
                    Modifier
                        .fillMaxWidth(),
                )
            }
        }

        Column(modifier = Modifier.padding(padding)) {
            Surface(
                shape = MaterialTheme.shapes.small,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                        .background(brush = brush)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight / 10)
                        .background(brush = brush)
                )
            }
        }
    }
}
