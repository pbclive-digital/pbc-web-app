package com.kavi.pbc.web.users.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.EmailGroupHeading
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_icon_delete
import pbcwebapp.ui_users_and_emails.generated.resources.email_group_label_tip_delete

@Composable
fun EmailGroupItem(
    modifier: Modifier = Modifier,
    emailGroup: EmailGroupHeading,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .shadow(
                elevation = 8.dp,
                spotColor = themeAdditionalColors.shadow,
                shape = RoundedCornerShape(8.dp),
            )
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onSelect.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emailGroup.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                AppTooltipWrap(
                    tipLabel = stringResource(Res.string.email_group_label_tip_delete)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.email_group_icon_delete),
                        contentDescription = "Delete News",
                        tint = themeAdditionalColors.shadow,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .clickable {
                                onDelete.invoke()
                            }
                    )
                }
            }
        }
    }
}