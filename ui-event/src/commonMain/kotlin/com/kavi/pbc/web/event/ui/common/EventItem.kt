package com.kavi.pbc.web.event.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.icon_event_pbc

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun EventItem(modifier: Modifier = Modifier, screenMaxWidth: Dp, event: Event, onClick: () -> Unit) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    BoxWithConstraints (
        modifier = modifier
            .padding(8.dp)
    ) {

        val cardSize = if (screenMaxWidth.value * 0.8 >= 400) {
            400.dp
        } else {
            (screenMaxWidth.value * 0.8).dp
        }

        Card(
            modifier = Modifier
                .width(cardSize)
                .height(cardSize)
                .clickable {
                    onClick.invoke()
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Background Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(event.eventImage)
                        .crossfade(true)
                        .build(),
                    error = painterResource(Res.drawable.icon_event_pbc),
                    contentDescription = null, // decorative image
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                )

                // 2. Gradient Overlay for text legibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, themeAdditionalColors.inverseDefault.copy(alpha = .9f)),
                                startY = cardSize.value / 2
                            )
                        )
                )

                // 3. Text content layered on top
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = event.name,
                        color = themeAdditionalColors.inverseOnBackground,
                        fontFamily = PBCFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier
                            .width((cardSize.value * 0.65).dp),
                        text = event.description,
                        color = themeAdditionalColors.inverseOnBackground,
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "on ${event.getFormatDate()} at ${event.getPlace()}",
                        color = themeAdditionalColors.inverseOnBackground,
                        fontFamily = PBCFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                    )
                }
            }
        }
    }
}