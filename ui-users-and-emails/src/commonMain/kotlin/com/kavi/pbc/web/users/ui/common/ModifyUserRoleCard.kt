package com.kavi.pbc.web.users.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppDropDownMenu
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserType
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_modify_role
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_resident_monk
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_role_modify
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_user_type
import pbcwebapp.ui_users_and_emails.generated.resources.user_phrase_modify_role

@Composable
fun ModifyUserRoleCard(
    user: User,
    onModifyUserRole: (newUserRole: String, isResidentMonk: Boolean) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val userType = remember { mutableStateOf(user.userType.name) }
    var isResidentMonkChecked by remember { mutableStateOf(user.residentMonk) }

    Card(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = themeAdditionalColors.shadow
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.user_label_modify_role),
                fontFamily = PBCFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Text(
                text = stringResource(Res.string.user_phrase_modify_role),
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            AppDropDownMenu(
                modifier = Modifier
                    .padding(top = 12.dp),
                title = stringResource(Res.string.user_label_user_type).uppercase(),
                selectableItems = listOf(
                    UserType.ADMIN.name, UserType.MANAGER.name,
                    UserType.MONK.name, UserType.CONSUMER.name),
                selectedItem = userType,
            )

            if (userType.value == UserType.MONK.name) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.user_label_resident_monk),
                        fontFamily = PBCFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Checkbox(
                        checked = isResidentMonkChecked,
                        onCheckedChange = { newCheckedState ->
                            isResidentMonkChecked = newCheckedState
                        }
                    )
                }
            }

            AppFilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = stringResource(Res.string.user_label_role_modify)
            ) {
                onModifyUserRole.invoke(userType.value, isResidentMonkChecked)
            }
        }
    }
}