package com.kavi.pbc.web.users.ui.users.manage.dialog

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
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.user_label_delete_confirmation
import pbcwebapp.ui_admin.generated.resources.user_label_no
import pbcwebapp.ui_admin.generated.resources.user_label_yes
import pbcwebapp.ui_admin.generated.resources.user_phrase_delete_confirmation

@Composable
fun DeleteUserConfirmationDialog(
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
        DeleteConfirmationContent(
            onConfirm = onConfirm, onDismiss = onDismiss
        )
    }
}

@Composable
private fun DeleteConfirmationContent(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.user_label_delete_confirmation),
                fontSize = 24.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.user_phrase_delete_confirmation),
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
                    label = stringResource(Res.string.user_label_yes)
                ) {
                    onConfirm.invoke()
                }
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = stringResource(Res.string.user_label_no)
                ) {
                    onDismiss.invoke()
                }
            }
        }
    }
}