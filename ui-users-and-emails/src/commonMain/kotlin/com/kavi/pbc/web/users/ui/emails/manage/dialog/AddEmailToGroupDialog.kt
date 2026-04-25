package com.kavi.pbc.web.users.ui.emails.manage.dialog

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
import com.kavi.pbc.web.data.email.EmailItem
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_add_email
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_add_to_group
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_cancel
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_email
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_owner_name
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_phrase_add_to_group

@Composable
fun AddEmailToEmailGroupDialog(
    showDialog: MutableState<Boolean>,
    onCreate: (emailItem: EmailItem) -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(500.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        AddEmailContent(
            onCreate, onDismiss
        )
    }
}

@Composable
private fun AddEmailContent(
    onCreate: (emailItem: EmailItem) -> Unit,
    onDismiss: () -> Unit
) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val ownerName = remember { mutableStateOf(TextFieldValue("")) }

    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.email_group_label_add_to_group),
                fontSize = 24.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.email_group_phrase_add_to_group),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.email_group_label_email).uppercase(),
                contentText = email,
                keyboardType = KeyboardType.Email,
                onValueChange = { newValue ->
                    email.value = newValue
                }
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                headingText = stringResource(Res.string.email_group_label_owner_name).uppercase(),
                contentText = ownerName,
                onValueChange = { newValue ->
                    ownerName.value = newValue
                }
            )

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.email_group_label_add_email)
                ) {
                    onCreate.invoke(EmailItem(
                        email = email.value.text, ownerName = ownerName.value.text
                    ))
                }

                AppOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = stringResource(Res.string.email_group_label_cancel)
                ) {
                    onDismiss.invoke()
                }
            }
        }
    }
}