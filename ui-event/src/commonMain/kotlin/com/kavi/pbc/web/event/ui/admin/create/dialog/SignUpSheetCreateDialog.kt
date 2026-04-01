package com.kavi.pbc.web.event.ui.admin.create.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.signup.SignUpSheet
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_label_cancel
import pbcwebapp.ui_event.generated.resources.event_label_create
import pbcwebapp.ui_event.generated.resources.event_label_is_allow_multi_sign_ups
import pbcwebapp.ui_event.generated.resources.event_label_new_sign_up_sheet
import pbcwebapp.ui_event.generated.resources.event_label_sheet_available_count
import pbcwebapp.ui_event.generated.resources.event_label_sheet_desc
import pbcwebapp.ui_event.generated.resources.event_label_sheet_name
import pbcwebapp.ui_event.generated.resources.event_phrase_new_sign_up_sheet
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun SignUpSheetCreateDialog(
    showDialog: MutableState<Boolean>,
    onCreate: (signUpSheetItem: SignUpSheet) -> Unit,
    onCancel: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(600.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onCancel.invoke()
        }
    ) {
        SignUpSheetCreateContent(
            onCreate = onCreate,
            onCancel = onCancel
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun SignUpSheetCreateContent(onCreate: (signUpSheetItem: SignUpSheet) -> Unit, onCancel: () -> Unit) {

    val signUpSheetName = remember { mutableStateOf(TextFieldValue("")) }
    val signUpSheetDescription = remember { mutableStateOf(TextFieldValue("")) }
    val signUpAvailabilityCount = remember { mutableStateOf(TextFieldValue("")) }
    var multiSignUpStatus by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.event_label_new_sign_up_sheet),
                fontSize = 24.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.event_phrase_new_sign_up_sheet),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.event_label_sheet_name).uppercase(),
                contentText = signUpSheetName,
                onValueChange = { newValue ->
                    signUpSheetName.value = newValue
                }
            )

            AppOutlineMultiLineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(150.dp),
                headingText = stringResource(Res.string.event_label_sheet_desc).uppercase(),
                maxLines = 8,
                contentText = signUpSheetDescription,
                onValueChange = { newValue ->
                    signUpSheetDescription.value = newValue
                }
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                headingText = stringResource(Res.string.event_label_sheet_available_count).uppercase(),
                contentText = signUpAvailabilityCount,
                keyboardType = KeyboardType.Number,
                onValueChange = { newValue ->
                    signUpAvailabilityCount.value = newValue
                }
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = stringResource(Res.string.event_label_is_allow_multi_sign_ups),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.weight(1f))

                Checkbox(
                    checked = multiSignUpStatus,
                    onCheckedChange = { newCheckedState ->
                        multiSignUpStatus = newCheckedState
                    }
                )
            }

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.event_label_create)
                ) {
                    onCreate.invoke(
                        SignUpSheet(
                            Uuid.random().toString(),
                            signUpSheetName.value.text,
                            signUpSheetDescription.value.text,
                            signUpAvailabilityCount.value.text.toInt(),
                            multiSignUpStatus
                        )
                    )
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