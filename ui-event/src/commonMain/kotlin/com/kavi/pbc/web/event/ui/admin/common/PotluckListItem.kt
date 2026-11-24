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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.potluck.PotluckItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_accept
import pbcwebapp.ui_event.generated.resources.event_icon_delete
import pbcwebapp.ui_event.generated.resources.event_icon_edit
import pbcwebapp.ui_event.generated.resources.event_label_potluck_item_count
import pbcwebapp.ui_event.generated.resources.event_label_potluck_item_name
import pbcwebapp.ui_event.generated.resources.event_label_tip_modify

@Composable
fun PotluckItemUI(
    modifier: Modifier = Modifier,
    potluckItem: PotluckItem,
    isCreate: Boolean = false,
    onCreate: ((potluckItem: PotluckItem) -> Unit)? = null,
    onCreateCancel: (() -> Unit)? = null,
    onModify: (potluckItem: PotluckItem) -> Unit,
    onDelete: (potluckItem: PotluckItem) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    var isModifying by remember { mutableStateOf(false) }
    val itemName = remember { mutableStateOf(TextFieldValue(potluckItem.itemName)) }
    val itemCount = remember { mutableStateOf(TextFieldValue(potluckItem.itemCount.toString())) }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (isModifying || isCreate) {
            Row (
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .weight(.75f)
            ) {
                AppOutlineTextField (
                    headingText = stringResource(Res.string.event_label_potluck_item_name).uppercase(),
                    contentText = itemName,
                    onValueChange = { newValue ->
                        itemName.value = newValue
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                AppOutlineTextField (
                    headingText = stringResource(Res.string.event_label_potluck_item_count).uppercase(),
                    contentText = itemCount,
                    onValueChange = { newValue ->
                        itemCount.value = newValue
                    }
                )
            }
        } else {
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
        }

        Row (
            modifier = Modifier.weight(.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AppTooltipWrap(
                modifier = Modifier.weight(.5f),
                tipLabel = stringResource(Res.string.event_label_tip_modify)
            ) {
                Icon(
                    painter = if (isModifying || isCreate)
                        painterResource(Res.drawable.event_icon_accept)
                    else
                        painterResource(Res.drawable.event_icon_edit),
                    contentDescription = "Edit Event",
                    tint = themeAdditionalColors.shadow,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (isCreate) {
                                onCreate?.invoke(
                                    PotluckItem(
                                        itemId = potluckItem.itemId,
                                        itemName = itemName.value.text,
                                        itemCount = itemCount.value.text.toInt()
                                    )
                                )
                            } else if (isModifying) {
                                isModifying = false
                                onModify.invoke(PotluckItem(
                                    itemId = potluckItem.itemId,
                                    itemName = itemName.value.text,
                                    itemCount = itemCount.value.text.toInt()
                                ))
                            } else {
                                isModifying = true
                            }
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
                            if (!isCreate)
                                onDelete.invoke(potluckItem)
                            else
                                onCreateCancel?.invoke()
                        }
                )
            }
        }
    }
}
