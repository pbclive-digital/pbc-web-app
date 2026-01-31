package com.kavi.pbc.web.dashboard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.dashboard.data.model.TabItem
import com.kavi.pbc.web.dashboard.ui.event.EventsUI
import com.kavi.pbc.web.dashboard.ui.news.NewsUI
import com.kavi.pbc.web.dashboard.ui.appointment.AppointmentUI
import com.kavi.pbc.web.dashboard.ui.question.QuestionUI
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.icon_appointment
import pbcwebapp.ui_dashboard.generated.resources.icon_ask_question
import pbcwebapp.ui_dashboard.generated.resources.icon_dashboard_profile
import pbcwebapp.ui_dashboard.generated.resources.icon_event
import pbcwebapp.ui_dashboard.generated.resources.icon_news
import pbcwebapp.ui_dashboard.generated.resources.image_dhamma_chakra_256
import pbcwebapp.ui_dashboard.generated.resources.image_pbc
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_pbc
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_profile
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_sign_in
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_sign_out
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_sign_up

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardUI(navController: NavController) {

    var appAuthStatus by remember {
        mutableStateOf(AppLocalStore.shared.retrieveValue<AppAuthStatus>(key = DataKey.APP_USER_AUTH_STATUS))
    }

    var isExpanded by remember { mutableStateOf(false) }
    val showSignUpDialog = remember { mutableStateOf(false) }

    val authTabItemList = listOf(
        /*TabItem(name = "Home", icon = Res.drawable.icon_lotus),*/ // TODO - Keep this for future use
        TabItem(name = "Events", icon = Res.drawable.icon_event),
        TabItem(name = "News", icon = Res.drawable.icon_news),
        TabItem(name = "Appointments", icon = Res.drawable.icon_appointment),
        TabItem(name = "Questions", icon = Res.drawable.icon_ask_question)
    )

    val unauthTabItemList = listOf(
        /*TabItem(name = "Home", icon = Res.drawable.icon_lotus),*/ // TODO - Keep this for future use
        TabItem(name = "Events", icon = Res.drawable.icon_event),
        TabItem(name = "News", icon = Res.drawable.icon_news),
        TabItem(name = "Questions", icon = Res.drawable.icon_ask_question)
    )

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    BoxWithConstraints(
        modifier = Modifier
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

        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row (
                        modifier = Modifier.padding(start = sidePadding, end = sidePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box (
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(90.dp),
                                painter = painterResource(Res.drawable.image_pbc),
                                contentDescription = "PBC image with name"
                            )
                        }

                        Column (
                            modifier = Modifier.padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(Res.string.label_dashboard_pbc),
                                fontFamily = PBCFontFamily,
                                fontSize = 32.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

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
                                when(appAuthStatus) {
                                    AppAuthStatus.SIGN_IN -> {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                                .data(Session.user?.profilePicUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Profile Picture",
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(Res.drawable.icon_dashboard_profile),
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(5.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                    else -> {
                                        Image(
                                            painterResource(Res.drawable.image_dhamma_chakra_256),
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
                                when(appAuthStatus) {
                                    AppAuthStatus.SIGN_IN -> {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Res.string.label_dashboard_profile)) },
                                            onClick = {
                                                // Invoke user authentication
                                                if (Session.isLogIn()) {
                                                    // Navigate to profile screen
                                                    println("Profile Tap")
                                                }

                                                isExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Res.string.label_dashboard_sign_out)) },
                                            onClick = {
                                                // Invoke user authentication
                                                if (Session.isLogIn()) {
                                                    // Open up profile screen
                                                    println("User already in - open up profile screen")
                                                    ContractServiceLocator.locate(AuthContract::class).signOut()
                                                    appAuthStatus = AppAuthStatus.NONE
                                                }

                                                isExpanded = false
                                            }
                                        )
                                    }
                                    AppAuthStatus.SIGN_UP_REQUIRED -> {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Res.string.label_dashboard_sign_up)) },
                                            onClick = {
                                                // Navigate to register screen
                                                showSignUpDialog.value = true
                                                isExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Res.string.label_dashboard_sign_out)) },
                                            onClick = {
                                                // Open up profile screen
                                                ContractServiceLocator.locate(AuthContract::class).signOut()
                                                appAuthStatus = AppAuthStatus.NONE
                                                isExpanded = false
                                            }
                                        )
                                    }
                                    else -> {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Res.string.label_dashboard_sign_in)) },
                                            onClick = {
                                                // Invoke sign-in with Firebase-Google
                                                ContractServiceLocator.locate(AuthContract::class).signInWithFirebaseGoogle()
                                                ContractServiceLocator.locate(AuthContract::class).retrieveCurrentAuthStatus { authStatus ->
                                                    appAuthStatus = authStatus
                                                    if (authStatus == AppAuthStatus.SIGN_UP_REQUIRED) {
                                                        // Navigate to register screen
                                                        showSignUpDialog.value = true
                                                    } else {
                                                        // Re-login and update auth status
                                                        AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
                                                    }
                                                }

                                                isExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

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
                            authTabItemList.forEachIndexed { index, tabItem ->
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
                            unauthTabItemList.forEachIndexed { index, tabItem ->
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

            TabContent(
                selectedTabIndex = selectedTabIndex,
                appAuthStatus,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .fillMaxSize(),
                navController = navController
            )
        }
    }
}

@Composable
fun TabContent(
    selectedTabIndex: Int,
    appAuthStatus: AppAuthStatus?,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    when(appAuthStatus) {
        AppAuthStatus.SIGN_IN -> {
            when (selectedTabIndex) {
                /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
                0 -> EventsUI(navController = navController)
                1 -> NewsUI(navController = navController)
                2 -> AppointmentUI(navController = navController)
                3 -> QuestionUI(navController = navController)
            }
        }
        else -> {
            when (selectedTabIndex) {
                /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
                0 -> EventsUI(navController = navController)
                1 -> NewsUI(navController = navController)
                2 -> QuestionUI(navController = navController)
            }
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