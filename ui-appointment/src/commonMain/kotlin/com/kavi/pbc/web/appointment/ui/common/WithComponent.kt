package com.kavi.pbc.web.appointment.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_icon_monk
import pbcwebapp.ui_appointment.generated.resources.appointment_image_pbc
import pbcwebapp.ui_appointment.generated.resources.appointment_label_any_monk

@Composable
fun WithComponent(modifier: Modifier = Modifier, user: User? = null, onSelect: (() -> Unit)? = null) {
    user?.let {
        Column (
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary),
                        shape = CircleShape
                    )
                    .clickable {
                        onSelect?.invoke()
                    }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(it.profilePicUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.appointment_icon_monk),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )
            }

            val name = if (it.userType == UserType.MONK) {
                "Bhanthe ${it.firstName} ${it.lastName}"
            } else {
                "${it.firstName} ${it.lastName}"
            }
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }?: run {
        Column (
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        onSelect?.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(90.dp),
                    painter = painterResource(Res.drawable.appointment_image_pbc),
                    contentDescription = "PBC image with name"
                )
            }

            Text(
                text = stringResource(Res.string.appointment_label_any_monk),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
