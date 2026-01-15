package com.kavi.pbc.web.event.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.icon_event_pbc

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun EventListItem(modifier: Modifier = Modifier, event: Event) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    BoxWithConstraints (
        modifier = modifier.padding(top = 2.dp)
    ) {
        val screenWidth = this.maxWidth

        Row (
            modifier = Modifier
                .padding(4.dp)
                .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
                .shadow(
                    elevation = 8.dp,
                    spotColor = themeAdditionalColors.shadow,
                    shape = RoundedCornerShape(8.dp),
                )
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = modifier
                    .padding(12.dp)
            ) {
                Text(
                    text = event.name,
                    fontFamily = PBCFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row (
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Column {
                        Text(
                            modifier = Modifier
                                .width((screenWidth.value * 0.65).dp),
                            text = event.description,
                            fontFamily = PBCFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = "on ${event.getFormatDate()} at ${event.getPlace()}",
                            fontFamily = PBCFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AsyncImage(
                        model = event.eventImage,
                        error = painterResource(Res.drawable.icon_event_pbc),
                        contentDescription = "Event image picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}