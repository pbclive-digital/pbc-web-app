package com.kavi.pbc.web.users.ui.emails.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.common.ui.component.AppOutlineButton
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.users.ui.common.SelectedFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_cancel
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_create
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_csv_template
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_new_group
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_new_group_name
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_no_required_fields_create
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_select_file
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_phrase_new_group

@Composable
fun CreateNewEmailGroupDialog(
    showDialog: MutableState<Boolean>,
    onCreate: (groupName: String, csvFile: PlatformFile) -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(700.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        CreateEmailGroupContent(
            onCreate, onDismiss
        )
    }
}

@Composable
private fun CreateEmailGroupContent(
    onCreate: (groupName: String, csvFile: PlatformFile) -> Unit,
    onDismiss: () -> Unit
) {
    val emailGroupName = remember { mutableStateOf(TextFieldValue("")) }
    var isFileSelected by remember { mutableStateOf(false) }

    val errorBalloonVisibility = remember { mutableStateOf(false) }

    // Open for URLs
    val uriHandler = LocalUriHandler.current

    var selectedCsvFile by remember { mutableStateOf<PlatformFile?>(null) }
    val scope = rememberCoroutineScope()
    val csvPickerLauncher = rememberFilePickerLauncher(
        type = FileKitType.File(),
        title = "Select Image for NEWS"
    ) { platformFile ->
        // Handle the selected file
        platformFile?.let { csvFile ->
            scope.launch {
                // Read the file and convert it to ImageBitmap
                selectedCsvFile = csvFile
                isFileSelected = true
            }
        }
    }

    Box (
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.email_group_label_new_group),
                fontSize = 32.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.email_group_phrase_new_group),
                fontSize = 18.sp,
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                AppLinkButton(
                    label = stringResource(Res.string.email_group_label_csv_template)
                ) {
                    // Download template
                    uriHandler.openUri("${Network.shared.getBaseUrl()}/email-group/download/template/email-group-template.csv")
                }
            }

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                // Error or Success message balloon
                ErrorMessageBalloon(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    showBalloon = errorBalloonVisibility,
                    errorMessage = stringResource(Res.string.email_group_label_no_required_fields_create),
                    onDismiss = {
                        errorBalloonVisibility.value = false
                    }
                )
            }

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.email_group_label_new_group_name).uppercase(),
                contentText = emailGroupName,
                keyboardType = KeyboardType.Email,
                onValueChange = { newValue ->
                    emailGroupName.value = newValue
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isFileSelected) {
                AppOutlineButton(
                    label = stringResource(Res.string.email_group_label_select_file)
                ) {
                    // Select file
                    csvPickerLauncher.launch()
                }
            } else {
                SelectedFile(fileName = "${selectedCsvFile?.name}") {
                    selectedCsvFile = null
                    isFileSelected = false
                }
            }

            Row (
                modifier = Modifier.padding(top = 16.dp),
            ) {
                AppFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = stringResource(Res.string.email_group_label_create)
                ) {
                    if (selectedCsvFile != null && emailGroupName.value.text.isNotEmpty()) {
                        onCreate.invoke(emailGroupName.value.text, selectedCsvFile!!)
                    } else {
                        errorBalloonVisibility.value = true
                    }
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