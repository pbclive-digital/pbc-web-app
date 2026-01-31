package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kavi.pbc.web.common.ui.model.ProfileActionConfig
import com.kavi.pbc.web.data.auth.AppAuthStatus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.icon_common_profile
import pbcwebapp.lib_common_ui.generated.resources.image_common_dhamma_chakra_256
import pbcwebapp.lib_common_ui.generated.resources.label_common_profile
import pbcwebapp.lib_common_ui.generated.resources.label_common_sign_in
import pbcwebapp.lib_common_ui.generated.resources.label_common_sign_out
import pbcwebapp.lib_common_ui.generated.resources.label_common_sign_up

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileActionComponent(
    profileActionConfig: ProfileActionConfig
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it}
    ) {
        Box (
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary),
                    shape = CircleShape
                )
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) {
            when(profileActionConfig.appAuthStatus) {
                AppAuthStatus.SIGN_IN -> {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(profileActionConfig.profileUserImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(Res.drawable.icon_common_profile),
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                    )
                }
                else -> {
                    Image(
                        painterResource(Res.drawable.image_common_dhamma_chakra_256),
                        contentDescription = "Dhamma chakkra",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                /*if (isExpanded)
                    isExpanded = false*/
            },
            modifier = Modifier
                .width(150.dp)
        ) {
            when(profileActionConfig.appAuthStatus) {
                AppAuthStatus.SIGN_IN -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_common_profile)) },
                        onClick = {
                            profileActionConfig.onProfileClick.invoke()
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_common_sign_out)) },
                        onClick = {
                            profileActionConfig.onSignOutClick.invoke()
                            isExpanded = false
                        }
                    )
                }
                AppAuthStatus.SIGN_UP_REQUIRED -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_common_sign_up)) },
                        onClick = {
                            // Navigate to register screen
                            profileActionConfig.onSignUpClick.invoke()
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_common_sign_out)) },
                        onClick = {
                            profileActionConfig.onSignOutClick.invoke()
                            isExpanded = false
                        }
                    )
                }
                else -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_common_sign_in)) },
                        onClick = {
                            // Invoke sign-in with Firebase-Google
                            profileActionConfig.onSignInClick.invoke()
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}