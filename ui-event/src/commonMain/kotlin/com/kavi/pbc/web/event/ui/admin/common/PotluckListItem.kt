package com.kavi.pbc.web.event.ui.admin.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.potluck.PotluckItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_delete
import pbcwebapp.ui_event.generated.resources.event_icon_edit
import pbcwebapp.ui_event.generated.resources.event_label_tip_modify

@Composable
fun PotluckItemUI(
    modifier: Modifier = Modifier,
    potluckItem: PotluckItem,
    onModify: () -> Unit,
    onDelete: (potluckItem: PotluckItem) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Row (
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .weight(.75f)
        ) {
            Text(
                text = potluckItem.itemName,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = ": ${potluckItem.itemCount}",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            )
        }

        Row (
            modifier = Modifier.weight(.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            /*Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp).weight(.2f),
                text = "Count: ${potluckItem.itemCount}",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            )*/
            AppTooltipWrap(
                modifier = Modifier.weight(.5f),
                tipLabel = stringResource(Res.string.event_label_tip_modify)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.event_icon_edit),
                    contentDescription = "Edit Event",
                    tint = themeAdditionalColors.shadow,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onModify.invoke()
                        }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            AppTooltipWrap(
                modifier = Modifier.weight(.5f),
                tipLabel = stringResource(Res.string.event_label_tip_modify)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.event_icon_delete),
                    contentDescription = "Edit Event",
                    tint = themeAdditionalColors.shadow,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onDelete.invoke(potluckItem)
                        }
                )
            }
        }
    }
}
