package com.kavi.pbc.web.event.ui.admin.create.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineButton
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.potluck.PotluckItem
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_label_add_to_potluck
import pbcwebapp.ui_event.generated.resources.event_label_cancel
import pbcwebapp.ui_event.generated.resources.event_label_create
import pbcwebapp.ui_event.generated.resources.event_label_potluck_item_count
import pbcwebapp.ui_event.generated.resources.event_label_potluck_item_name
import pbcwebapp.ui_event.generated.resources.event_phrase_add_to_potluck
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun PotluckItemCreateDialog(
    showDialog: MutableState<Boolean>,
    onCreate: (potluckItem: PotluckItem) -> Unit,
    onCancel: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(600.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onCancel.invoke()
        }
    ) {
        PotluckItemCreateContent(
            onCreate = onCreate,
            onCancel = onCancel
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun PotluckItemCreateContent(onCreate: (potluckItem: PotluckItem) -> Unit, onCancel: () -> Unit) {

    val itemName = remember { mutableStateOf(TextFieldValue("")) }
    val itemAvailability = remember { mutableStateOf(TextFieldValue("")) }

    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.event_label_add_to_potluck),
                fontSize = 24.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.event_phrase_add_to_potluck),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.event_label_potluck_item_name).uppercase(),
                contentText = itemName,
                onValueChange = { newValue ->
                    itemName.value = newValue
                }
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                headingText = stringResource(Res.string.event_label_potluck_item_count).uppercase(),
                contentText = itemAvailability,
                keyboardType = KeyboardType.Number,
                onValueChange = { newValue ->
                    itemAvailability.value = newValue
                }
            )

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.event_label_create)
                ) {
                    onCreate.invoke(PotluckItem(Uuid.random().toString(),
                        itemName.value.text, itemAvailability.value.text.toInt()))
                }

                AppOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = stringResource(Res.string.event_label_cancel)
                ) {
                    onCancel.invoke()
                }
            }
        }
    }
}