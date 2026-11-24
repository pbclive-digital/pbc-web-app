package com.kavi.pbc.web.event.ui.admin.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
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
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.signup.SignUpSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_accept
import pbcwebapp.ui_event.generated.resources.event_icon_delete
import pbcwebapp.ui_event.generated.resources.event_icon_edit
import pbcwebapp.ui_event.generated.resources.event_label_is_allow_multi_sign_ups
import pbcwebapp.ui_event.generated.resources.event_label_sheet_available_count
import pbcwebapp.ui_event.generated.resources.event_label_sheet_desc
import pbcwebapp.ui_event.generated.resources.event_label_sheet_name
import pbcwebapp.ui_event.generated.resources.event_label_tip_modify

@Composable
fun SignUpSheetListItem(
    modifier: Modifier = Modifier,
    signUpSheet: SignUpSheet,
    isCreate: Boolean = false,
    onCreate: ((signUpSheet: SignUpSheet) -> Unit)? = null,
    onCreateCancel: (() -> Unit)? = null,
    onModify: (signUpSheet: SignUpSheet) -> Unit,
    onDelete: (signUpSheet: SignUpSheet) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    var isModifying by remember { mutableStateOf(false) }
    val sheetName = remember { mutableStateOf(TextFieldValue(signUpSheet.sheetName)) }
    val sheetDescription = remember { mutableStateOf(TextFieldValue(signUpSheet.sheetDescription)) }
    val availableCount = remember { mutableStateOf(TextFieldValue(signUpSheet.availableCount.toString())) }
    var multiSignUpStatus by remember { mutableStateOf(signUpSheet.allowMultiSignUps) }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (isModifying || isCreate) {
            Column (
                modifier = Modifier
                    .height(190.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .weight(.75f)
            ) {
                Row (
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .weight(.85f)
                ) {
                    AppOutlineTextField (
                        modifier = Modifier.weight(.75f),
                        headingText = stringResource(Res.string.event_label_sheet_name).uppercase(),
                        contentText = sheetName,
                        onValueChange = { newValue ->
                            sheetName.value = newValue
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    AppOutlineTextField (
                        modifier = Modifier.weight(.25f),
                        headingText = stringResource(Res.string.event_label_sheet_available_count).uppercase(),
                        contentText = availableCount,
                        onValueChange = { newValue ->
                            availableCount.value = newValue
                        }
                    )
                }

                AppOutlineMultiLineTextField (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    headingText = stringResource(Res.string.event_label_sheet_desc).uppercase(),
                    maxLines = 8,
                    contentText = sheetDescription,
                    onValueChange = { newValue ->
                        sheetDescription.value = newValue
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
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
            }
        } else {
            Row (
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .weight(.85f)
            ) {
                Text(
                    text = "${signUpSheet.availableCount}: ",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = signUpSheet.sheetName,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row (
            modifier = Modifier.weight(.15f),
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
                                    SignUpSheet(
                                        sheetId = signUpSheet.sheetId,
                                        sheetName = sheetName.value.text,
                                        sheetDescription = sheetDescription.value.text,
                                        availableCount = availableCount.value.text.toInt(),
                                        allowMultiSignUps = multiSignUpStatus
                                    )
                                )
                            } else if (isModifying) {
                                isModifying = false
                                onModify.invoke(
                                    SignUpSheet(
                                        sheetId = signUpSheet.sheetId,
                                        sheetName = sheetName.value.text,
                                        sheetDescription = sheetDescription.value.text,
                                        availableCount = availableCount.value.text.toInt(),
                                        allowMultiSignUps = multiSignUpStatus
                                    )
                                )
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
                                onDelete.invoke(signUpSheet)
                            else
                                onCreateCancel?.invoke()
                        }
                )
            }
        }
    }
}
