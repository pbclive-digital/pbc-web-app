package com.kavi.pbc.web.users.ui.users.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.users.ui.common.AdminUserUI
import com.kavi.pbc.web.users.ui.users.manage.dialog.ViewUserDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.user_icon_x
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_admin_users
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_consumer_users
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_manage
import pbcwebapp.ui_users_and_emails.generated.resources.user_phrase_manage

@Composable
fun UserManageUI(navController: NavController) {

    val viewModel: UserManageViewModel = viewModel { UserManageViewModel() }

    val adminUsers by viewModel.adminUserList.collectAsState()

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    LaunchedEffect(Unit) {
        viewModel.fetchAdmins()
        viewModel.fetchConsumers()
    }

    val showUserViewDialog = mutableStateOf(false)
    val selectedUser = remember { mutableStateOf(User(email = "")) }

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
            Title(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                titleText = stringResource(Res.string.user_label_manage)
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 30.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_phrase_manage),
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
                    ) {
                        Text(
                            text = stringResource(Res.string.user_label_admin_users),
                            fontFamily = PBCFontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth()
                        )

                        LazyColumn {
                            itemsIndexed(adminUsers) { index, adminUser ->
                                AdminUserUI(
                                    user = adminUser,
                                    onView = {
                                        showUserViewDialog.value = true
                                        selectedUser.value = adminUser
                                    }
                                )
                                if (index < adminUsers.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = themeAdditionalColors.shadow
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column (modifier = Modifier.weight(.65f)) {
                        ConsumerUserUI(
                            modifier = Modifier.padding(start = 8.dp),
                            viewModel = viewModel,
                            showUserViewDialog = showUserViewDialog,
                            selectedUser = selectedUser
                        )
                    }
                }
            }
        }
    }

    ViewUserDialog(
        showDialog = showUserViewDialog,
        user = selectedUser,
        onDismiss = {
            showUserViewDialog.value = false
        }
    )
}

@Composable
private fun ConsumerUserUI(
    modifier: Modifier = Modifier,
    viewModel: UserManageViewModel,
    showUserViewDialog: MutableState<Boolean>,
    selectedUser: MutableState<User>
) {
    val letterGroupedConsumerList by viewModel.letterGroupedConsumerList.collectAsState()

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
                titleText = stringResource(Res.string.user_label_consumer_users),
                textSize = 36,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                letterGroupedConsumerList.keys.forEach { key ->
                    UserGridContainer(
                        letter = key,
                        userList = letterGroupedConsumerList[key],
                        onView = { user ->
                            showUserViewDialog.value = true
                            selectedUser.value = user
                        },
                        onDelete = { user ->
                            // TODO - DELETE USER
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserGridContainer(letter: Char, userList: List<User>?, onView: (user: User) -> Unit,
                              onDelete: (user: User) -> Unit) {

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
            userList?.let {
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

                    it.forEach { user ->
                        AssistChip(
                            modifier = Modifier.padding(4.dp),
                            label = { Text("${user.firstName} ${user.lastName}")},
                            onClick = {
                                onView.invoke(user)
                            },
                            trailingIcon = {
                                Icon(
                                    painterResource(Res.drawable.user_icon_x),
                                    contentDescription = "Localized description",
                                    modifier = Modifier
                                        .size(AssistChipDefaults.IconSize)
                                        .clickable {
                                            onDelete.invoke(user)
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