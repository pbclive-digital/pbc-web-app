package com.kavi.pbc.web.users.ui.broadcast.send

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.component.pbc.EmailGroupSelection
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.users.data.model.SendBroadcastUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.broadcast_icon_close_x
import pbcwebapp.ui_admin.generated.resources.broadcast_label_create
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_message
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_subject
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_title
import pbcwebapp.ui_admin.generated.resources.broadcast_label_send_invalid_form
import pbcwebapp.ui_admin.generated.resources.broadcast_label_send_new_broadcast
import pbcwebapp.ui_admin.generated.resources.broadcast_label_send_new_broadcast_failure
import pbcwebapp.ui_admin.generated.resources.broadcast_label_send_no_email_groups

@Composable
fun SendBroadcastEmailDialog(
    showDialog: MutableState<Boolean>,
    onSendBroadcast: () -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(800.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        SendBroadcastContent(
            onSendBroadcast = onSendBroadcast,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun SendBroadcastContent(
    onSendBroadcast: () -> Unit,
    onDismiss: () -> Unit
) {
    val viewModel: SendBroadcastViewModel = viewModel { SendBroadcastViewModel() }

    val emailGroupHeadings by viewModel.emailGroupHeadings.collectAsState()
    val sendBroadcastState by viewModel.sendBroadcastState.collectAsState()

    val subject = remember { mutableStateOf(TextFieldValue("")) }
    val title = remember { mutableStateOf(TextFieldValue("")) }
    val message = remember { mutableStateOf(TextFieldValue("")) }
    val selectedHeadings = remember { mutableStateOf(mutableListOf<EmailGroupHeading>()) }

    LaunchedEffect(Unit) {
        viewModel.fetchEmailGroupHeadings()
    }

    LaunchedEffect(selectedHeadings) {
        viewModel.updateSelectedEmailGroups(selectedHeadings.value)
    }

    val errorBalloonVisibility = remember { mutableStateOf(false) }
    var errorBalloonMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 30.dp)
                    .verticalScroll(state = rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleWithAction(
                    titleText = stringResource(Res.string.broadcast_label_send_new_broadcast),
                    actionPainter = painterResource(Res.drawable.broadcast_icon_close_x),
                    actionPainterSize = 40.dp,
                    isIcon = true,
                ) {
                    errorBalloonVisibility.value = false
                    viewModel.revokeSendBroadcastUiState()

                    // Dismiss the dialog
                    onDismiss.invoke()
                }

                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    // Error or Success message balloon
                    ErrorMessageBalloon(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        showBalloon = errorBalloonVisibility,
                        errorMessage = errorBalloonMessage,
                        onDismiss = {
                            errorBalloonVisibility.value = false
                            viewModel.revokeSendBroadcastUiState()
                        }
                    )
                }

                Column (
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    AppOutlineTextField (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        headingText = stringResource(Res.string.broadcast_label_email_subject).uppercase(),
                        contentText = subject,
                        onValueChange = { newValue ->
                            subject.value = newValue
                            viewModel.updateEmailSubject(subject.value.text)
                        }
                    )

                    AppOutlineTextField (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        headingText = stringResource(Res.string.broadcast_label_email_title).uppercase(),
                        contentText = title,
                        onValueChange = { newValue ->
                            title.value = newValue
                            viewModel.updateEmailTitle(title.value.text)
                        }
                    )

                    EmailGroupSelection(
                        emailHeadings = emailGroupHeadings,
                        selectedHeadings = selectedHeadings
                    )

                    AppOutlineMultiLineTextField (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(250.dp),
                        headingText = stringResource(Res.string.broadcast_label_email_message).uppercase(),
                        contentText = message,
                        maxLines = 20,
                        onValueChange = { newValue ->
                            message.value = newValue
                            viewModel.updateEmailMessage(message.value.text)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppFilledButton(
                        label = stringResource(Res.string.broadcast_label_create),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        viewModel.sendBroadcastEmail()
                    }
                }
            }
        }
    }

    when(sendBroadcastState) {
        SendBroadcastUiState.FAILURE -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.broadcast_label_send_new_broadcast_failure)
        }
        SendBroadcastUiState.EMPTY_EMAIL_GROUP -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.broadcast_label_send_no_email_groups)
        }
        SendBroadcastUiState.FORM_INVALID -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.broadcast_label_send_invalid_form)
        }
        SendBroadcastUiState.SUCCESS -> {
            onSendBroadcast.invoke()
        }
        else -> {
            /* Nothing to do */
        }
    }
}