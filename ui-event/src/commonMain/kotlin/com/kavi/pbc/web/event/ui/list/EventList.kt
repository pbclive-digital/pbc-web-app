package com.kavi.pbc.web.event.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.PageContainer
import com.kavi.pbc.web.common.ui.model.ProfileActionConfig
import com.kavi.pbc.web.event.ui.common.EventItem
import com.kavi.pbc.web.event.ui.common.EventListItem
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.EventPath
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.label_event_past
import pbcwebapp.ui_event.generated.resources.label_event_upcoming

@Composable
fun EventListUI(navController: NavController, isContainerRequired: Boolean = false) {

    val viewModel: EventListViewModel = viewModel { EventListViewModel() }

    LaunchedEffect(Unit) {
        viewModel.fetchAppAuthStatus()
    }

    val appAuthStatus by viewModel.appAuthStatus.collectAsState()

    val showSignUpDialog = remember { mutableStateOf(false) }

    if (isContainerRequired) {
        PageContainer(
            profileActionConfig = ProfileActionConfig(
                appAuthStatus = appAuthStatus,
                profileUserImageUrl = Session.user?.profilePicUrl,
                onProfileClick = {
                    if (Session.isLogIn()) {
                        // Navigate to profile screen
                        println("Profile Tap")
                    }
                },
                onSignOutClick = {
                    // Invoke user authentication
                    if (Session.isLogIn()) {
                        ContractServiceLocator.locate(AuthContract::class).signOut()
                        viewModel.updateAuthStatus(AppAuthStatus.NONE)
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
                            viewModel.updateAuthStatus(authStatus)
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
        ) {
            PageContent(navController = navController, viewModel = viewModel)
        }
    } else {
        PageContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
private fun PageContent(navController: NavController, viewModel: EventListViewModel) {

    val upcomingEventList by viewModel.upcomingEventList.collectAsState()
    val pastEventList by viewModel.pastEventList.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth

        LaunchedEffect(Unit) {
            viewModel.fetchUpcomingEvents()
            when (UIUtil.screenType(maxWidth)) {
                ScreenType.PHONE -> viewModel.fetchPastEventsWithLimit()
                ScreenType.TABLET, ScreenType.COMPUTER -> viewModel.fetchPastEvents()
            }
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
                text = stringResource(Res.string.label_event_upcoming),
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                upcomingEventList.forEach { event ->
                    EventItem(screenMaxWidth = maxWidth, event = event, onClick = {
                        /**
                         * Alternative way to do the same navigation as a path
                         * navController.navigate("event/event-selected/${event.id}")
                         */
                        navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                    })
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
                text = stringResource(Res.string.label_event_past),
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Row (
                modifier = Modifier.height(600.dp)
            ) {
                val columCount = when (UIUtil.screenType(maxWidth)) {
                    ScreenType.PHONE -> 1
                    ScreenType.TABLET, ScreenType.COMPUTER -> {
                        3
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columCount),
                ) {
                    items(pastEventList) { event ->
                        EventListItem(
                            event = event,
                            modifier = Modifier.clickable {
                                /**
                                 * Alternative way to do the same navigation as a path
                                 * navController.navigate("event/event-selected/${event.id}")
                                 */
                                navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                            }
                        )
                    }
                }
            }
        }
    }
}