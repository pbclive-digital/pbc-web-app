package com.kavi.pbc.web.users.ui.broadcast.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.kavi.pbc.live.data.model.broadcast.EmailTemplateType
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.record.EmailRecord
import com.kavi.pbc.web.data.email.record.EmailRecordContent
import com.kavi.pbc.web.users.data.model.EmailRecordUiState
import com.kavi.pbc.web.users.ui.common.EmailRecordItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.broadcast_icon_send
import pbcwebapp.ui_admin.generated.resources.broadcast_label_create
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_empty_list
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_event_agenda
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_event_desc
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_event_url
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_message
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_subject
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_template
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_title
import pbcwebapp.ui_admin.generated.resources.broadcast_label_email_to
import pbcwebapp.ui_admin.generated.resources.broadcast_label_manage
import pbcwebapp.ui_admin.generated.resources.broadcast_phrase_manage

@Composable
fun BroadcastManageUI(
    navController: NavController
) {
    val viewModel: BroadcastManageViewModel = viewModel { BroadcastManageViewModel() }

    val emailRecordUiState by viewModel.emailRecordUiState.collectAsState()
    val emailRecordList by viewModel.emailRecordList.collectAsState()

    var selectedPagerIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberPagerState { 2 }

    val selectedEmailRecord = remember { mutableStateOf(EmailRecord(
        emailTemplate = EmailTemplateType.BROADCAST,
        emailRecordContent = EmailRecordContent()
    )) }

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    LaunchedEffect(Unit) {
        viewModel.fetchEmailRecords()
    }

    LaunchedEffect(emailRecordList) {
        if (emailRecordList.isNotEmpty())
            selectedEmailRecord.value = emailRecordList[0]
    }

    LaunchedEffect(selectedPagerIndex) {
        state.animateScrollToPage(selectedPagerIndex)
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
                titleText = stringResource(Res.string.broadcast_label_manage)
            ) {
                AppButtonWithIcon(
                    modifier = Modifier.padding(bottom = 12.dp),
                    label = stringResource(Res.string.broadcast_label_create),
                    icon = painterResource(Res.drawable.broadcast_icon_send),
                    cornerRadius = 12.dp
                ) {
                    //showCreateEmailGroupDialog.value = true
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 30.dp)
            ) {
                Text(
                    text = stringResource(Res.string.broadcast_phrase_manage),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Column (
                        modifier = Modifier
                            .weight(.35f)
                            .padding(top = 20.dp, start = 15.dp)
                    ) {
                        when(emailRecordUiState) {
                            EmailRecordUiState.SUCCESS -> {
                                LazyColumn {
                                    itemsIndexed(emailRecordList) { index, emailRecord ->
                                        EmailRecordItem(emailRecord = emailRecord) {
                                            // Set selected email record
                                            selectedEmailRecord.value = emailRecord
                                        }
                                        if (index < emailRecordList.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.fillMaxWidth(),
                                                thickness = 1.dp,
                                                color = themeAdditionalColors.shadow
                                            )
                                        }
                                    }
                                    item {
                                        LaunchedEffect(selectedPagerIndex) {
                                            viewModel.fetchEmailRecords()
                                        }
                                    }
                                }
                            }
                            else -> {
                                EmptyEmailList(stringResource(Res.string.broadcast_label_email_empty_list))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(.65f)
                            .padding(top = 10.dp, start = 15.dp)
                    ) {
                        SelectedEmailRecord(
                            viewModel = viewModel,
                            selectedEmailRecord = selectedEmailRecord
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedEmailRecord(
    modifier: Modifier = Modifier,
    viewModel: BroadcastManageViewModel,
    selectedEmailRecord: MutableState<EmailRecord>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Title(
                titleText = selectedEmailRecord.value.emailRecordContent.subject,
                textSize = 36,
                textColor = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "sent on ${selectedEmailRecord.value.getFormatSentDate()}",
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                EmailContentItem(
                    itemName = stringResource(Res.string.broadcast_label_email_subject),
                    itemContent = {
                        ItemContentWrapper {
                            Text(
                                text = selectedEmailRecord.value.emailRecordContent.subject,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                            )
                        }
                    }
                )

                EmailContentItem(
                    itemName = stringResource(Res.string.broadcast_label_email_title),
                    itemContent = {
                        ItemContentWrapper {
                            Text(
                                text = selectedEmailRecord.value.emailRecordContent.title,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                            )
                        }
                    }
                )

                EmailContentItem(
                    itemName = stringResource(Res.string.broadcast_label_email_to),
                    itemContent = {
                        ItemContentWrapper {
                            FlowRow(
                                maxItemsInEachRow = 10,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                selectedEmailRecord.value.emailGroupHeadings.forEach { heading ->
                                    SuggestionChip(
                                        modifier = Modifier.padding(2.dp),
                                        label = {
                                            Text(
                                                text = heading.name,
                                                fontFamily = PBCFontFamily,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        onClick = {
                                            // Nothing to Do
                                        }
                                    )
                                }
                            }
                        }
                    }
                )

                EmailContentItem(
                    itemName = stringResource(Res.string.broadcast_label_email_template),
                    itemContent = {
                        ItemContentWrapper {
                            Text(
                                text = selectedEmailRecord.value.emailTemplate.name,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                            )
                        }
                    }
                )

                EmailContentItem(
                    itemName = stringResource(Res.string.broadcast_label_email_message),
                    itemContent = {
                        ItemContentWrapper {
                            Text(
                                text = selectedEmailRecord.value.emailRecordContent.message,
                                fontFamily = PBCFontFamily,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                            )
                        }
                    }
                )

                when(selectedEmailRecord.value.emailTemplate) {
                    EmailTemplateType.BROADCAST -> {
                        /* Nothing for Now */
                    }
                    EmailTemplateType.NEW_EVENT -> {
                        selectedEmailRecord.value.emailRecordContent.eventDescription?.let {
                            EmailContentItem(
                                itemName = stringResource(Res.string.broadcast_label_email_event_desc),
                                itemContent = {
                                    ItemContentWrapper {
                                        Text(
                                            text = it,
                                            fontFamily = PBCFontFamily,
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            )
                        }

                        if (selectedEmailRecord.value.emailRecordContent.eventAgenda.isNotEmpty()) {
                            EmailContentItem(
                                itemName = stringResource(Res.string.broadcast_label_email_event_agenda),
                                itemContent = {
                                    ItemContentWrapper {
                                        Column {
                                            selectedEmailRecord.value.emailRecordContent.eventAgenda.forEach { agendaItem ->
                                                Text(
                                                    text = agendaItem,
                                                    fontFamily = PBCFontFamily,
                                                    fontSize = 18.sp,
                                                    textAlign = TextAlign.Start,
                                                    fontWeight = FontWeight.Normal,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        selectedEmailRecord.value.emailRecordContent.eventUrl?.let {
                            EmailContentItem(
                                itemName = stringResource(Res.string.broadcast_label_email_event_url),
                                itemContent = {
                                    ItemContentWrapper {
                                        Column {
                                            Text(
                                                text = it,
                                                fontFamily = PBCFontFamily,
                                                fontSize = 18.sp,
                                                textAlign = TextAlign.Start,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemContentWrapper(
    content: @Composable () -> Unit
) {
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
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun EmailContentItem(
    modifier: Modifier = Modifier,
    itemName: String,
    itemContent: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row {
            Text(
                modifier = Modifier
                    .weight(.15f)
                    .padding(top = 8.dp),
                text = "$itemName:",
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column (
                modifier = Modifier
                    .weight(.85f)
            ) {
                itemContent()
            }
        }
    }
}

@Composable
fun EmptyEmailList(emptyMessage: String) {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = emptyMessage,
            textAlign = TextAlign.Center,
        )
    }
}