package com.kavi.pbc.web.auth.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_auth.generated.resources.Res
import pbcwebapp.ui_auth.generated.resources.auth_label_address
import pbcwebapp.ui_auth.generated.resources.auth_label_basic_info
import pbcwebapp.ui_auth.generated.resources.auth_label_edit
import pbcwebapp.ui_auth.generated.resources.auth_label_email
import pbcwebapp.ui_auth.generated.resources.auth_label_name
import pbcwebapp.ui_auth.generated.resources.auth_label_phone_num
import pbcwebapp.ui_auth.generated.resources.auth_label_user_favorite
import pbcwebapp.ui_auth.generated.resources.auth_phrase_basic_info
import pbcwebapp.ui_auth.generated.resources.auth_phrase_profile_pic
import pbcwebapp.ui_auth.generated.resources.auth_phrase_user_favorite

@Composable
fun UserProfileUI(navController: NavController) {

    val viewModel: UserProfileViewModel = viewModel { UserProfileViewModel() }

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val maxWidth = this.maxWidth

        when (UIUtil.screenType(maxWidth)) {
            ScreenType.PHONE -> PhoneUI(viewModel = viewModel)
            ScreenType.TABLET, ScreenType.COMPUTER -> WebUI(viewModel = viewModel)
        }
    }
}

@Composable
private fun PhoneUI(viewModel: UserProfileViewModel) {

    val profileUser by viewModel.userProfile.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    model = profileUser.profilePicUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        BasicInfoCard(profileUser = profileUser)

        UserFavorites()
    }
}

@Composable
private fun WebUI(viewModel: UserProfileViewModel) {
    val profileUser by viewModel.userProfile.collectAsState()

    Row (
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(.35f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
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
                    model = profileUser.profilePicUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(220.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = stringResource(Res.string.auth_phrase_profile_pic),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }
        Column (
            modifier = Modifier
                .weight(.65f)
                .verticalScroll(rememberScrollState())
        ) {
            BasicInfoCard(profileUser = profileUser)

            UserFavorites()
        }
    }
}

@Composable
private fun BasicInfoCard(profileUser: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                TitleWithActionComposable(
                    titleText = stringResource(Res.string.auth_label_basic_info),
                    textSize = 32,
                ) {
                    AppLinkButton(
                        label = stringResource(Res.string.auth_label_edit),
                        labelTextSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    ) {
                        // Open dialog box to edit details
                        //val profileKey = profileLocalRepository.setModifyingProfile(profileUser)
                        //navController.navigate("profile/profile-update/$profileKey")
                    }
                }
            }

            Text(
                text = stringResource(Res.string.auth_phrase_basic_info),
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
                    text = stringResource(Res.string.auth_label_email),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = profileUser.email,
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.auth_label_name),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${profileUser.firstName} ${profileUser.lastName}",
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.auth_label_phone_num),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = profileUser.phoneNumber ?: run { "" },
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.auth_label_address),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = profileUser.address ?: run { "" },
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Composable
private fun UserFavorites() {
    Card(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Title(
                titleText = stringResource(Res.string.auth_label_user_favorite),
                textSize = 32
            )

            Text(
                text = stringResource(Res.string.auth_phrase_user_favorite),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )

            Text(
                text = " --- Coming Soon --- ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}