package com.kavi.pbc.web.users.ui.emails.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.users.ui.common.EmailGroupItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_icon_create
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_icon_delete
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_create
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_manage
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_phrase_manage

@Composable
fun EmailGroupManageUI(
    navController: NavController
) {
    val viewModel: EmailGroupManageViewModel = viewModel { EmailGroupManageViewModel() }
    val selectedEmailGroupHeading = remember { mutableStateOf(EmailGroupHeading()) }
    val isInitialEmailGroupHeadingSelected = remember { mutableStateOf(false) }

    val emailGroupHeadings by viewModel.emailGroupHeadings.collectAsState()

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    LaunchedEffect(Unit) {
        viewModel.fetchEmailGroupHeadings()
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
                    //showCreateOrModifyDialog.value = true
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
                    /*Column (
                        modifier = Modifier
                            .weight(.35f)
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        emailGroupHeadings.forEachIndexed { index, emailGroup ->
                            EmailGroupItem(emailGroup = emailGroup) {

                            }
                            if (index < emailGroupHeadings.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = themeAdditionalColors.shadow
                                )
                            }
                        }
                    }*/
                    LazyColumn (
                        modifier = Modifier
                            .weight(.35f)
                    ) {
                        items(emailGroupHeadings) { emailGroup ->
                            EmailGroupItem(emailGroup = emailGroup, onDelete = {}, onSelect = {
                                selectedEmailGroupHeading.value = emailGroup
                            })
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
}

@Composable
private fun SelectedEmailGroup(
    modifier: Modifier = Modifier,
    viewModel: EmailGroupManageViewModel,
    selectedEmailGroupHeading: MutableState<EmailGroupHeading>
) {
    val letterGroupedEmailList = viewModel.letterGroupedEmailList.collectAsState()

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
                // TODO: Add two buttons to 'Add' & 'Remove' emails from selected email group
            }

            Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                letterGroupedEmailList.value.keys.forEach { key ->
                    EmailGridContainer(key, letterGroupedEmailList.value[key])
                }
            }
        }
    }
}

@Composable
private fun EmailGridContainer(letter: Char, emailList: List<EmailItem>?) {
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
                        SuggestionChip(modifier = Modifier.padding(4.dp), label = { Text(item.email)}, onClick = {})
                    }
                }
            }
        }
    }
}