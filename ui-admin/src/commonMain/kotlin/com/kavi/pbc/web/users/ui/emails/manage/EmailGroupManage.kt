package com.kavi.pbc.web.users.ui.emails.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.users.ui.common.EmailGroupItem
import com.kavi.pbc.web.users.ui.emails.create.CreateNewEmailGroupDialog
import com.kavi.pbc.web.users.ui.emails.manage.dialog.AddEmailToEmailGroupDialog
import com.kavi.pbc.web.users.ui.emails.manage.dialog.DeleteEmailGroupConfirmationDialog
import com.kavi.pbc.web.users.ui.emails.manage.dialog.RemoveEmailConfirmationDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.email_group_icon_add_email
import pbcwebapp.ui_admin.generated.resources.email_group_icon_create
import pbcwebapp.ui_admin.generated.resources.email_group_icon_x
import pbcwebapp.ui_admin.generated.resources.email_group_label_create
import pbcwebapp.ui_admin.generated.resources.email_group_label_manage
import pbcwebapp.ui_admin.generated.resources.email_group_label_tip_add_email
import pbcwebapp.ui_admin.generated.resources.email_group_phrase_manage

@Composable
fun EmailGroupManageUI(
    navController: NavController
) {
    val viewModel: EmailGroupManageViewModel = viewModel { EmailGroupManageViewModel() }
    val selectedEmailGroupHeading = remember { mutableStateOf(EmailGroupHeading()) }

    val emailGroupHeadings by viewModel.emailGroupHeadings.collectAsState()

    val showCreateEmailGroupDialog = mutableStateOf(false)

    val showDeleteEmailGroupConfirmationDialog = mutableStateOf(false)
    var deletingEmailGroupId by mutableStateOf("")

    LaunchedEffect(Unit) {
        viewModel.fetchEmailGroupHeadings()
    }

    LaunchedEffect(emailGroupHeadings) {
        if(emailGroupHeadings.isNotEmpty()) {
            selectedEmailGroupHeading.value = emailGroupHeadings[0]
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 12.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TitleWithActionComposable(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                titleText = stringResource(Res.string.email_group_label_manage)
            ) {
                AppButtonWithIcon(
                    modifier = Modifier.padding(bottom = 12.dp),
                    label = stringResource(Res.string.email_group_label_create),
                    icon = painterResource(Res.drawable.email_group_icon_create),
                    cornerRadius = 12.dp
                ) {
                    showCreateEmailGroupDialog.value = true
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 30.dp)
            ) {
                Text(
                    text = stringResource(Res.string.email_group_phrase_manage),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    LazyColumn (
                        modifier = Modifier
                            .weight(.35f)
                    ) {
                        Session.config?.generalEmailGroup?.let { generalEmailGroup ->
                            item {
                                // GENERAL EMAIL GROUP
                                EmailGroupItem(emailGroup = generalEmailGroup, hideDelete = true,
                                    onDelete = {}, onSelect = {
                                    selectedEmailGroupHeading.value = generalEmailGroup
                                })
                            }
                            items(emailGroupHeadings) { emailGroup ->
                                // SKIP GENERAL EMAIL GROUP
                                if (emailGroup.id != generalEmailGroup.id) {
                                    EmailGroupItem(emailGroup = emailGroup, onDelete = {
                                        deletingEmailGroupId = emailGroup.id
                                        showDeleteEmailGroupConfirmationDialog.value = true
                                    }, onSelect = {
                                        selectedEmailGroupHeading.value = emailGroup
                                    })
                                }
                            }
                        }?: run {
                            // WHEN GENERAL EMAIL GROUP CAN NOT FIND IN SESSION
                            items(emailGroupHeadings) { emailGroup ->
                                EmailGroupItem(emailGroup = emailGroup, onDelete = {
                                    deletingEmailGroupId = emailGroup.id
                                    showDeleteEmailGroupConfirmationDialog.value = true
                                }, onSelect = {
                                    selectedEmailGroupHeading.value = emailGroup
                                })
                            }
                        }
                    }

                    Column (modifier = Modifier.weight(.65f)) {
                        SelectedEmailGroup(
                            modifier = Modifier.padding(start = 8.dp),
                            viewModel = viewModel,
                            selectedEmailGroupHeading = selectedEmailGroupHeading
                        )
                    }
                }
            }
        }
    }

    CreateNewEmailGroupDialog(
        showDialog = showCreateEmailGroupDialog,
        onCreate = { emailGroupName, file ->
            viewModel.createEmailGroupWithCSVFile(groupName = emailGroupName, uploadedCsvFile = file)
        },
        onDismiss = {
            showCreateEmailGroupDialog.value = false
        }
    )

    DeleteEmailGroupConfirmationDialog(
        showDialog = showDeleteEmailGroupConfirmationDialog,
        onConfirm = {
            viewModel.deleteEmailGroup(deletingEmailGroupId)
            deletingEmailGroupId = ""
        },
        onDismiss = {
            showDeleteEmailGroupConfirmationDialog.value = false
            deletingEmailGroupId = ""
        }
    )
}

@Composable
private fun SelectedEmailGroup(
    modifier: Modifier = Modifier,
    viewModel: EmailGroupManageViewModel,
    selectedEmailGroupHeading: MutableState<EmailGroupHeading>
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val letterGroupedEmailList = viewModel.letterGroupedEmailList.collectAsState()
    val selectedGroupEmailCount by viewModel.selectedGroupEmailCount.collectAsState()

    val showAddEmailDialog = mutableStateOf(false)
    val showRemoveEmailDialog = mutableStateOf(false)
    var deletingEmailItem by remember { mutableStateOf(EmailItem("", null)) }

    LaunchedEffect(selectedEmailGroupHeading.value) {
        viewModel.fetchEmailGroupEmailList(groupId = selectedEmailGroupHeading.value.id)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            TitleWithActionComposable(
                titleText = selectedEmailGroupHeading.value.name,
                textSize = 36,
                textColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$selectedGroupEmailCount",
                            fontFamily = PBCFontFamily,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    AppTooltipWrap(
                        tipLabel = stringResource(Res.string.email_group_label_tip_add_email)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.email_group_icon_add_email),
                            contentDescription = "Edit Email group",
                            tint = themeAdditionalColors.shadow,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .clickable {
                                    showAddEmailDialog.value = true
                                }
                        )
                    }
                }
            }

            Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                letterGroupedEmailList.value.keys.forEach { key ->
                    EmailGridContainer(
                        letter = key,
                        emailList = letterGroupedEmailList.value[key]
                    ) { emailItem ->
                        deletingEmailItem = emailItem
                        showRemoveEmailDialog.value = true
                    }
                }
            }
        }
    }

    AddEmailToEmailGroupDialog(
        showDialog = showAddEmailDialog,
        onCreate = { emailItem ->
            viewModel.addEmailToEmailGroup(
                selectedGroupId = selectedEmailGroupHeading.value.id,
                emailItem = emailItem
            )
            showAddEmailDialog.value = false
        },
        onDismiss = {
            showAddEmailDialog.value = false
        }
    )

    RemoveEmailConfirmationDialog(
        showDialog = showRemoveEmailDialog,
        onConfirm = {
            viewModel.removeEmailFromEmailGroup(
                selectedGroupId = selectedEmailGroupHeading.value.id,
                emailItem = deletingEmailItem
            )
        },
        onDismiss = {
            showRemoveEmailDialog.value = false
        }
    )
}

@Composable
private fun EmailGridContainer(letter: Char, emailList: List<EmailItem>?,
                               onDelete: (emailItem: EmailItem) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(8.dp))
            .clip( RoundedCornerShape(8.dp)),
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            emailList?.let {
                FlowRow(
                    maxItemsInEachRow = 10,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp),
                        text = "\"${letter.uppercaseChar()}\"",
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = PBCFontFamily,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                    it.forEach { item ->
                        AssistChip(
                            modifier = Modifier.padding(4.dp),
                            label = { Text(item.email)},
                            onClick = { /*Nothing to do*/ },
                            trailingIcon = {
                                Icon(
                                    painterResource(Res.drawable.email_group_icon_x),
                                    contentDescription = "Localized description",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable {
                                            onDelete.invoke(item)
                                        }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}