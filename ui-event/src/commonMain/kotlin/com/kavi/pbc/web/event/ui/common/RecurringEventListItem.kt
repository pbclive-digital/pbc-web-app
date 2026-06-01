package com.kavi.pbc.web.event.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_buddha_book

@Composable
fun RecurringEventListItem(modifier: Modifier = Modifier, event: Event, onViewMode: () -> Unit) {
    BoxWithConstraints {
        val maxWidth = this.maxWidth

        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable {
                    onViewMode.invoke()
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column (
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(Res.drawable.event_icon_buddha_book),
                    contentDescription = "Buddha Book icon",
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = event.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (maxWidth.value > 500) {
                    Row {
                        Text(
                            text = "on every ",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )

                        Text(
                            text = event.recurringDay.name,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = " at ",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )

                        Text(
                            text = event.startTime,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Column {
                        Row {
                            Text(
                                text = "on every ",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Text(
                                text = event.recurringDay.name,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Row {
                            Text(
                                text = "at ",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Text(
                                text = event.startTime,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}