package com.kavi.pbc.web.users.ui.broadcast.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.broadcast_icon_send
import pbcwebapp.ui_admin.generated.resources.broadcast_label_create
import pbcwebapp.ui_admin.generated.resources.broadcast_label_manage

@Composable
fun BroadcastManageUI(
    navController: NavController
) {
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
        }
    }
}