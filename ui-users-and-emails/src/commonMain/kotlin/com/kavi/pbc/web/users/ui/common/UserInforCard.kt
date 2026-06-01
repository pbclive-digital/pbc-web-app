package com.kavi.pbc.web.users.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_users_and_emails.generated.resources.Res
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_address
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_basic_info
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_email
import pbcwebapp.ui_users_and_emails.generated.resources.user_label_phone
import pbcwebapp.ui_users_and_emails.generated.resources.user_phrase_basic_info

@Composable
fun BasicUserInfoCard(user: User) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

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
                text = stringResource(Res.string.user_label_basic_info),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                fontSize = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.user_phrase_basic_info),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_label_email),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                SelectionContainer {
                    Text(
                        text = user.email,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                    )
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_label_phone),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                SelectionContainer {
                    Text(
                        text = user.phoneNumber ?: run { "" },
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                    )
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_label_address),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                SelectionContainer {
                    Text(
                        text = user.address ?: run { "" },
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}