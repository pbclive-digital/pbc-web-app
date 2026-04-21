package com.kavi.pbc.web.pbc.container

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.DashboardPath
import com.kavi.pbc.web.pbc.container.model.ProfileActionConfig
import com.kavi.pbc.web.pbc.container.model.TabItem
import com.kavi.pbc.web.pbc.container.ui.AdditionalActionComponent
import com.kavi.pbc.web.pbc.container.ui.ProfileActionComponent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_pbc_container.generated.resources.Res
import pbcwebapp.lib_pbc_container.generated.resources.container_image_pbc
import pbcwebapp.lib_pbc_container.generated.resources.container_label_pbc_name
import pbcwebapp.lib_pbc_container.generated.resources.container_label_pbc_name_short

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PBCDashboardContainer(
    modifier: Modifier = Modifier,
    navController: NavController,
    authTabItemList: MutableState<MutableList<TabItem>>,
    user: User,
    isAdminUser: Boolean,
    unAuthTabItemList: List<TabItem>,
    tabContent: @Composable (selectedTabIndex: Int, appAuthStatus: AppAuthStatus?) -> Unit
) {
    val showSignUpDialog = remember { mutableStateOf(false) }

    var appAuthStatus by remember {
        mutableStateOf(
            AppLocalStore.shared.retrieveValue<AppAuthStatus>(key = DataKey.APP_USER_AUTH_STATUS)
                ?: run { AppAuthStatus.NONE }
        )
    }

    val profileActionConfig = ProfileActionConfig(
            appAuthStatus = appAuthStatus,
            profileUserImageUrl = user.profilePicUrl,
            onSignOutClick = {
                // Invoke user authentication
                if (Session.isLogIn()) {
                    ContractServiceLocator.locate(AuthContract::class).signOut()
                    appAuthStatus = AppAuthStatus.NONE
                }
            },
            onSignUpClick = {
                // Navigate to register screen
                showSignUpDialog.value = true
            },
            onSignInClick = {
                // Invoke sign-in with Firebase-Google
                ContractServiceLocator.locate(AuthContract::class).signInWithFirebaseGoogle()
                ContractServiceLocator.locate(AuthContract::class)
                    .retrieveCurrentAuthStatus { authStatus ->
                        appAuthStatus = authStatus
                        if (authStatus == AppAuthStatus.SIGN_UP_REQUIRED) {
                            // Navigate to register screen
                            showSignUpDialog.value = true
                        } else {
                            // Re-login and update auth status
                            AppLocalStore.shared.storeValue(
                                DataKey.APP_USER_AUTH_STATUS,
                                authStatus
                            )
                        }
                    }
            }
        )

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    if (showSignUpDialog.value) {
        ContractServiceLocator.locate(AuthContract::class).ProvideRegisterUI(
            showDialog = showSignUpDialog,
            onAuthenticated = {
                appAuthStatus = AppAuthStatus.SIGN_IN
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, appAuthStatus)
                showSignUpDialog.value = false
            },
            onCreatedWithoutAuth = {
                appAuthStatus = AppAuthStatus.FAILED
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, appAuthStatus)
                showSignUpDialog.value = false
            },
            onCancel = {
                showSignUpDialog.value = false
            }
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth)

        val sidePadding = when (screenType) {
            ScreenType.PHONE -> 8.dp
            ScreenType.TABLET, ScreenType.COMPUTER -> {
                (maxWidth.value * .1).dp
            }
        }

        // Hide the Admin panel from PHONE screen type
        if (screenType == ScreenType.PHONE && isAdminUser && authTabItemList.value.size == 5) {
            authTabItemList.value.removeLast()
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.padding(start = sidePadding, end = sidePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(90.dp),
                                painter = painterResource(Res.drawable.container_image_pbc),
                                contentDescription = "PBC image with name"
                            )
                        }

                        Column(
                            modifier = Modifier.padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            when (screenType) {
                                ScreenType.PHONE -> {
                                    Title(
                                        titleText = stringResource(Res.string.container_label_pbc_name_short),
                                        textSize = 32,
                                        textColor = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }

                                else -> {
                                    Title(
                                        titleText = stringResource(Res.string.container_label_pbc_name),
                                        textSize = 32,
                                        textColor = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Column (
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // TODO: Uncomment this when it ready
                            // AdditionalActionComponent(navController = navController)

                            Spacer(modifier = Modifier.height(12.dp))

                            ProfileActionComponent(profileActionConfig = profileActionConfig)
                        }
                    }
                }
            }
        }

        Column (
            modifier = Modifier
                .padding(top = 160.dp, start = sidePadding, end = sidePadding)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .weight(.85f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp), spotColor = MaterialTheme.colorScheme.scrim)
                ) {
                    when(appAuthStatus) {
                        AppAuthStatus.SIGN_IN -> {
                            authTabItemList.value.forEachIndexed { index, tabItem ->
                                NavigationBarItem(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    colors = navigationBarColors(),
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    label = { Text(tabItem.name) },
                                    icon = {
                                        Icon(
                                            painterResource(tabItem.icon),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .width(45.dp)
                                                .height(45.dp)
                                                .padding(8.dp),
                                        )
                                    }
                                )
                            }
                        }
                        else -> {
                            unAuthTabItemList.forEachIndexed { index, tabItem ->
                                NavigationBarItem(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    colors = navigationBarColors(),
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    label = { Text(tabItem.name) },
                                    icon = {
                                        Icon(
                                            painterResource(tabItem.icon),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .width(45.dp)
                                                .height(45.dp)
                                                .padding(8.dp),
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            tabContent(selectedTabIndex, appAuthStatus)
        }
    }
}

@Composable
fun navigationBarColors(): NavigationBarItemColors {
    return NavigationBarItemColors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.secondary,
        unselectedTextColor = MaterialTheme.colorScheme.secondary,
        selectedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledIconColor = Color.Gray,
        disabledTextColor = Color.Gray,
    )
}