package com.kavi.pbc.web.users.ui.emails.manage.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineButton
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_no
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_remove_confirmation
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_yes
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_phrase_remove_confirmation

@Composable
fun RemoveEmailConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(500.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        RemoveEmailConfirmationContent(
            onConfirm, onDismiss
        )
    }
}

@Composable
private fun RemoveEmailConfirmationContent(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.email_group_label_remove_confirmation),
                fontSize = 24.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.email_group_phrase_remove_confirmation),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.email_group_label_yes)
                ) {
                    onConfirm.invoke()
                }
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = stringResource(Res.string.email_group_label_no)
                ) {
                    onDismiss.invoke()
                }
            }
        }
    }
}