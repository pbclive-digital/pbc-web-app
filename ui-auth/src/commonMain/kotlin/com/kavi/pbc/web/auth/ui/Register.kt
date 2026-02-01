package com.kavi.pbc.web.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kavi.pbc.web.auth.data.model.UserRegisterUiState
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_auth.generated.resources.Res
import pbcwebapp.ui_auth.generated.resources.auth_icon_x
import pbcwebapp.ui_auth.generated.resources.auth_label_address
import pbcwebapp.ui_auth.generated.resources.auth_label_first_name
import pbcwebapp.ui_auth.generated.resources.auth_label_last_name
import pbcwebapp.ui_auth.generated.resources.auth_label_phone_num
import pbcwebapp.ui_auth.generated.resources.auth_label_register
import pbcwebapp.ui_auth.generated.resources.auth_phrase_welcome_msg

@Composable
fun RegisterDialog(
    showDialog: MutableState<Boolean>,
    onAuthenticated: () -> Unit,
    onCreatedWithoutAuth: () -> Unit,
    onCancel: () -> Unit
) {
    AppBasicDialog(
        showDialog = showDialog.value,
        onDismissRequest = {
            onCancel.invoke()
        }
    ) {
        RegisterUI(
            onAuthenticated = onAuthenticated,
            onCreatedWithoutAuth = onCreatedWithoutAuth,
            onCancel = onCancel
        )
    }
}

@Composable
fun RegisterUI(onAuthenticated: () -> Unit,
               onCreatedWithoutAuth: () -> Unit, onCancel: () -> Unit) {

    val viewModel: RegisterViewModel = viewModel { RegisterViewModel() }

    val signedUser by viewModel.signedUser.collectAsState()
    val userRegisterUiState by viewModel.userRegisterUiState.collectAsState()

    LaunchedEffect(Unit) {
        // Create the user from sign-in email
        viewModel.createUserFromFirebaseAuth()
    }

    val firstName = remember { mutableStateOf(TextFieldValue()) }
    val lastName = remember { mutableStateOf(TextFieldValue()) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue()) }
    val address = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(signedUser) {
        signedUser.firstName?.let {
            firstName.value = TextFieldValue(it)
        }
        signedUser.lastName?.let {
            lastName.value = TextFieldValue(it)
        }
        signedUser.phoneNumber?.let {
            phoneNumber.value = TextFieldValue(it)
        }
        signedUser.address?.let {
            address.value = TextFieldValue(it)
        }
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth

        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        Column (
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())

        ) {
            when(screenType) {
                ScreenType.PHONE -> {
                    TitleWithAction(
                        titleText = stringResource(Res.string.auth_label_register),
                        textSize = 40,
                        actionPainter = painterResource(Res.drawable.auth_icon_x),
                        actionPainterSize = 30.dp,
                        isIcon = true,
                    ) {
                        onCancel.invoke()
                    }
                }
                else -> {
                    TitleWithAction(
                        titleText = stringResource(Res.string.auth_label_register),
                        actionPainter = painterResource(Res.drawable.auth_icon_x),
                        actionPainterSize = 40.dp,
                        isIcon = true,
                    ) {
                        onCancel.invoke()
                    }
                }
            }

            Text(
                text = stringResource(Res.string.auth_phrase_welcome_msg),
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
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
                        model = signedUser.profilePicUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                text = signedUser.email,
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.auth_label_first_name).uppercase(),
                contentText = firstName,
                onValueChange = { newValue ->
                    firstName.value = newValue
                    viewModel.updateUserFirstName(firstName.value.text)
                }
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.auth_label_last_name).uppercase(),
                contentText = lastName,
                onValueChange = { newValue ->
                    lastName.value = newValue
                    viewModel.updateUserLastName(lastName.value.text)
                }
            )

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.auth_label_phone_num).uppercase(),
                contentText = phoneNumber,
                onValueChange = { newValue ->
                    phoneNumber.value = newValue
                    viewModel.updateUserPhoneNum(phoneNumber.value.text)
                }
            )

            AppOutlineMultiLineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.auth_label_address).uppercase(),
                maxLines = 5,
                contentText = address,
                onValueChange = { newValue ->
                    address.value = newValue
                    viewModel.updateUserAddress(address.value.text)
                }
            )

            AppFilledButton(
                label = stringResource(Res.string.auth_label_register),
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                viewModel.registerNewUser()
            }
        }

        when(userRegisterUiState) {
            UserRegisterUiState.NONE -> {}
            UserRegisterUiState.FAILED -> {}
            UserRegisterUiState.PENDING -> {
                AppFullScreenLoader()
            }
            UserRegisterUiState.AUTH_FAILED -> {
                onCreatedWithoutAuth.invoke()
                onCancel.invoke()
            }
            UserRegisterUiState.AUTHENTICATED -> {
                onAuthenticated.invoke()
                onCancel.invoke()
            }
        }
    }
}