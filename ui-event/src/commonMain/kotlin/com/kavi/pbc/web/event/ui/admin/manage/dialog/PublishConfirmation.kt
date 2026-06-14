package com.kavi.pbc.web.event.ui.admin.manage.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineButton
import com.kavi.pbc.web.common.ui.component.pbc.EmailGroupSelection
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.EmailGroupHeading
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_label_no
import pbcwebapp.ui_event.generated.resources.event_label_publish_email_group_select
import pbcwebapp.ui_event.generated.resources.event_label_publish_ready
import pbcwebapp.ui_event.generated.resources.event_label_yes
import pbcwebapp.ui_event.generated.resources.event_phrase_publish_email_group_select
import pbcwebapp.ui_event.generated.resources.event_phrase_publish_ready

@Composable
fun PublishConfirmationDialog(
    showDialog: MutableState<Boolean>,
    emailGroups: List<EmailGroupHeading>,
    onAgree: (selectedEmailGroups: List<EmailGroupHeading>) -> Unit,
    onDisagree: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(700.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDisagree.invoke()
        }
    ) {
        PublishConfirmationContent(
            emailGroups = emailGroups,
            onAgree = onAgree,
            onDisagree = onDisagree
        )
    }
}

@Composable
private fun PublishConfirmationContent(emailGroups: List<EmailGroupHeading>,
                                       onAgree: (selectedEmailGroups: List<EmailGroupHeading>) -> Unit,
                                       onDisagree: () -> Unit) {

    val selectedHeadings = remember { mutableStateOf(mutableListOf<EmailGroupHeading>()) }

    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.event_label_publish_ready),
                fontSize = 32.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.event_phrase_publish_ready),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.event_label_publish_email_group_select),
                fontSize = 20.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.event_phrase_publish_email_group_select),
                fontSize = 16.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            EmailGroupSelection(
                emailHeadings = emailGroups,
                selectedHeadings = selectedHeadings
            )

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.event_label_yes)
                ) {
                    onAgree.invoke(selectedHeadings.value)
                }

                AppOutlineButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = stringResource(Res.string.event_label_no)
                ) {
                    onDisagree.invoke()
                }
            }
        }
    }
}
