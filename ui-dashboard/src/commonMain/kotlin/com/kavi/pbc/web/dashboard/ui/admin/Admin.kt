package com.kavi.pbc.web.dashboard.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.dashboard.data.model.AdminAction
import com.kavi.pbc.web.dashboard.ui.common.AdminItem
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.EventContract
import com.kavi.pbc.web.parent.contract.model.NewsContract
import com.kavi.pbc.web.parent.contract.model.UsersAndEmailsContract
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.dashboard_phrase_unsupport_screen_type

@Composable
fun AdminUI(modifier: Modifier = Modifier, navController: NavController) {

    var selectedAction by remember { mutableStateOf(AdminAction.EVENT_MANAGEMENT) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        Row {
            when (UIUtil.screenType(maxWidth)) {
                ScreenType.PHONE -> {
                    // NOT SUPPORTING UI
                    ScreenTypeNotSupport()
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .weight(.15f)
                            .height(maxHeight)
                            .padding(top = 10.dp, end = 4.dp)
                    ) {
                        AdminItem(label = "Event Management") {
                            selectedAction = AdminAction.EVENT_MANAGEMENT
                        }
                        AdminItem(label = "NEWS Management") {
                            selectedAction = AdminAction.NEWS_MANAGEMENT
                        }
                        AdminItem(label = "Email Groups") {
                            selectedAction = AdminAction.EMAIL_GROUP_MANAGEMENT
                        }
                        AdminItem(label = "User Management") {
                            selectedAction = AdminAction.USER_MANAGEMENT
                        }
                        /*AdminItem(label = "Broadcast Messages") {
                            selectedAction = AdminAction.BROADCAST_MSG
                        }*/
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "App Version: ${Session.appVersion}",
                                fontFamily = PBCFontFamily,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(.85f)
                            .padding(top = 10.dp, start = 4.dp)
                    ) {
                        when(selectedAction) {
                            AdminAction.EVENT_MANAGEMENT -> {
                                ContractServiceLocator.locate(EventContract::class)
                                    .GetEventAdminUI(navController = navController)
                            }
                            AdminAction.USER_MANAGEMENT -> {
                                ContractServiceLocator.locate(UsersAndEmailsContract::class)
                                    .GetUserManageUI(navController = navController)
                            }
                            AdminAction.NEWS_MANAGEMENT -> {
                                ContractServiceLocator.locate(NewsContract::class)
                                    .GetNewsManage(navController = navController)
                            }
                            AdminAction.EMAIL_GROUP_MANAGEMENT -> {
                                ContractServiceLocator.locate(UsersAndEmailsContract::class)
                                    .GetEmailGroupManageUI(navController = navController)
                            }
                            AdminAction.BROADCAST_MSG -> {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                                    .background(MaterialTheme.colorScheme.background),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Yet to implement")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenTypeNotSupport() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.dashboard_phrase_unsupport_screen_type),
            fontFamily = PBCFontFamily,
            fontSize = 36.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}