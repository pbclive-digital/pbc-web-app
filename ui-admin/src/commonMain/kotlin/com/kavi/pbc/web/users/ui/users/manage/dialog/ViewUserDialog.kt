package com.kavi.pbc.web.users.ui.users.manage.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.users.ui.common.BasicUserInfoCard
import com.kavi.pbc.web.users.ui.common.ModifyUserRoleCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_admin.generated.resources.Res
import pbcwebapp.ui_admin.generated.resources.user_icon_x
import pbcwebapp.ui_admin.generated.resources.user_label_manage
import pbcwebapp.ui_admin.generated.resources.user_label_user

@Composable
fun ViewUserDialog(
    showDialog: MutableState<Boolean>,
    user: MutableState<User>,
    onModifyUserRole: (newUserRole: String, isResidentMonk: Boolean, user: User) -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.width(700.dp),
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        ViewUserContent(user = user.value, onModifyUserRole = onModifyUserRole, onDismiss = onDismiss)
    }
}

@Composable
private fun ViewUserContent(
    user: User,
    onModifyUserRole: (newUserRole: String, isResidentMonk: Boolean, user: User) -> Unit,
    onDismiss: () -> Unit
) {
    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 20.dp)
            .fillMaxWidth()
    ) {
        TitleWithAction(
            titleText = stringResource(Res.string.user_label_user),
            actionPainter = painterResource(Res.drawable.user_icon_x),
            actionPainterSize = 40.dp,
            isIcon = true,
        ) {
            onDismiss.invoke()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = CircleShape
                    )
            ) {
                AsyncImage(
                    model = user.profilePicUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = "${user.firstName} ${user.lastName}",
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        BasicUserInfoCard(user = user)

        if (Session.user?.id != user.id) {
            ModifyUserRoleCard(user = user, onModifyUserRole = { newUserRole, isResidentMonk ->
                onModifyUserRole.invoke(newUserRole, isResidentMonk, user)
            })
        }
    }
}