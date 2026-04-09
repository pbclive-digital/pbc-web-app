package com.kavi.pbc.web.event.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_delete
import pbcwebapp.ui_event.generated.resources.event_icon_edit
import pbcwebapp.ui_event.generated.resources.event_icon_publish
import pbcwebapp.ui_event.generated.resources.event_label_tip_delete
import pbcwebapp.ui_event.generated.resources.event_label_tip_modify
import pbcwebapp.ui_event.generated.resources.event_label_tip_publish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItemForAdmin(
    modifier: Modifier = Modifier,
    isDraftEvent: Boolean,
    event: Event, onModify: () -> Unit,
    onPublish: (() -> Unit)? = null,
    onDelete: () -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = event.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = PBCFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = "on ${event.getFormatDate()} at ${event.getPlace()}",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppTooltipWrap(
                    tipLabel = stringResource(Res.string.event_label_tip_modify)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.event_icon_edit),
                        contentDescription = "Edit Event",
                        tint = themeAdditionalColors.shadow,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .clickable {
                                onModify.invoke()
                            }
                    )
                }
                if (isDraftEvent) {
                    AppTooltipWrap(
                        tipLabel = stringResource(Res.string.event_label_tip_publish)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.event_icon_publish),
                            contentDescription = "Publish Event",
                            tint = themeAdditionalColors.shadow,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .clickable {
                                    onPublish?.invoke()
                                }
                        )
                    }
                }
                AppTooltipWrap(
                    tipLabel = stringResource(Res.string.event_label_tip_delete)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.event_icon_delete),
                        contentDescription = "Delete Event",
                        tint = themeAdditionalColors.shadow,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .clickable {
                                onDelete.invoke()
                            }
                    )
                }
            }
        }
    }
}