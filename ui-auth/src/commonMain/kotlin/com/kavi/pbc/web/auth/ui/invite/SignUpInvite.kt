package com.kavi.pbc.web.auth.ui.invite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_auth.generated.resources.Res
import pbcwebapp.ui_auth.generated.resources.auth_image_invite
import pbcwebapp.ui_auth.generated.resources.auth_label_close
import pbcwebapp.ui_auth.generated.resources.auth_label_join_us
import pbcwebapp.ui_auth.generated.resources.auth_phrase_join_us
import pbcwebapp.ui_auth.generated.resources.auth_phrase_join_us_guide

@Composable
fun SignUpInviteDialog(
    showDialog: MutableState<Boolean>,
    onCancel: () -> Unit
) {
    BoxWithConstraints {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        val modifier = when(screenType) {
            ScreenType.PHONE -> {
                Modifier
                    .fillMaxWidth()
            }
            else -> {
                Modifier
                    .width(500.dp)
            }
        }

        AppBasicDialog(
            modifier = modifier,
            showDialog = showDialog.value,
            onDismissRequest = {
                onCancel.invoke()
            }
        ) {
            SignUpInviteContent(onCancel = onCancel, screenType = screenType)
        }
    }
}

@Composable
fun SignUpInviteContent(onCancel: () -> Unit, screenType: ScreenType) {

    val modifier = when(screenType) {
        ScreenType.PHONE -> Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp)
            .fillMaxWidth()
        else -> Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(25.dp)
            .width(500.dp)
    }

    Box (
        modifier = modifier
    ) {

        Column {
            Text(
                text = stringResource(Res.string.auth_label_join_us),
                fontFamily = PBCFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.auth_image_invite),
                    contentDescription = "Contact us icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(260.dp)
                )
            }

            Text(
                text = stringResource(Res.string.auth_phrase_join_us),
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.auth_phrase_join_us_guide),
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )

            AppFilledButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(Res.string.auth_label_close)
            ) {
                onCancel.invoke()
            }
        }
    }
}