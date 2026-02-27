package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kavi.pbc.web.appointment.ui.common.AppointmentItem
import com.kavi.pbc.web.appointment.ui.common.AppointmentReqItem
import com.kavi.pbc.web.appointment.ui.create.request.RequestCreateOrModifyDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppMessageDialog
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_label_accepted_phone
import pbcwebapp.ui_appointment.generated.resources.appointment_label_create_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_dismiss
import pbcwebapp.ui_appointment.generated.resources.appointment_label_not_eligible_to_create
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_phone

@Composable
fun MobileContent(screenWidth: Dp, screenHeight: Dp, viewModel: AppointmentDashboardViewModel) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val showCreateAppointmentReqDialog = remember { mutableStateOf(false) }
    val showNotEligibleMessage = remember { mutableStateOf(false) }

    var selectedPagerIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberPagerState { 2 }

    val requestCreateEligibility by viewModel.appointmentReqCreateEligibility.collectAsState()

    LaunchedEffect(selectedPagerIndex) {
        state.animateScrollToPage(selectedPagerIndex)
    }

    Column {
        AppFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = stringResource(Res.string.appointment_label_create_request)
        ) {
            if (requestCreateEligibility.allowToCreateRequest) {
                showCreateAppointmentReqDialog.value = true
            } else {
                showNotEligibleMessage.value = true
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 12.dp, end = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedPagerIndex = 0
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.appointment_label_request_phone),
                    fontFamily = PBCFontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedPagerIndex = 1
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.appointment_label_accepted_phone),
                    fontFamily = PBCFontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
        ) {
            repeat(state.pageCount) { iteration ->
                val color = if (state.currentPage == iteration)
                    themeAdditionalColors.quaternary else MaterialTheme.colorScheme.surface

                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(2.dp)
                        .width((screenWidth - 20.dp) / 2)
                        .fillMaxWidth()
                        .background(color)
                )
            }
        }

        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
            snapPosition = SnapPosition.Center
        ) { page ->
            when (page) {
                0 -> AppointmentRequestList(screenHeight = screenHeight, viewModel = viewModel)
                1 -> AppointmentList(screenHeight = screenHeight, viewModel = viewModel)
            }
        }
    }

    if (showCreateAppointmentReqDialog.value) {
        RequestCreateOrModifyDialog(
            showDialog = showCreateAppointmentReqDialog,
            onCancel = { refreshRequired ->
                showCreateAppointmentReqDialog.value = false

                // Some update happens in appointment requests, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchAppointmentRequests()
                    viewModel.checkAppointmentReqCreateEligibility()
                }
            }
        )
    }

    if (showNotEligibleMessage.value) {
        AppMessageDialog(
            showDialog = showNotEligibleMessage,
            message = stringResource(Res.string.appointment_label_not_eligible_to_create),
            actionButtonText = stringResource(Res.string.appointment_label_dismiss)
        ) {
            showNotEligibleMessage.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppointmentRequestList(screenHeight: Dp, viewModel: AppointmentDashboardViewModel) {
    val appointmentRequestList by viewModel.appointmentReqList.collectAsState()

    val viewUserSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showViewSheet = remember { mutableStateOf(false) }
    val showModifyAppointmentReqDialog = remember { mutableStateOf(false) }
    var selectedAppointmentReq by remember { mutableStateOf(AppointmentRequest(user = User(email = ""))) }

    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight)
            .padding(bottom = 20.dp)
    ) {
        items(appointmentRequestList) { appointmentReq ->
            AppointmentReqItem(
                appointmentReq = appointmentReq,
                onModify = {
                    selectedAppointmentReq = appointmentReq
                    showModifyAppointmentReqDialog.value = true
                },
                onDelete = {
                    viewModel.deleteAppointmentRequest(appointmentReq.id!!)
                },
                onView = {
                    showViewSheet.value = true
                    selectedAppointmentReq = appointmentReq
                },
                onAccept = {
                    val appointment = Appointment(
                        title = appointmentReq.title,
                        userId = appointmentReq.userId,
                        user = appointmentReq.user,
                        selectedMonkId = appointmentReq.selectedMonkId,
                        selectedMonk = appointmentReq.selectedMonk,
                        reason = appointmentReq.reason
                    )
                }
            )
        }
    }

    if (showModifyAppointmentReqDialog.value) {
        RequestCreateOrModifyDialog(
            showDialog = showModifyAppointmentReqDialog,
            modifyRequest = selectedAppointmentReq,
            onCancel = { refreshRequired ->
                showModifyAppointmentReqDialog.value = false

                // Some update happens in appointment requests, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchAppointmentRequests()
                    viewModel.checkAppointmentReqCreateEligibility()
                }
            }
        )
    }
}

@Composable
private fun AppointmentList(screenHeight: Dp, viewModel: AppointmentDashboardViewModel) {

    val appointmentList by viewModel.appointmentList.collectAsState()

    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight)
            .padding(bottom = 20.dp)
    ) {
        items(appointmentList) { appointment ->
            AppointmentItem(
                appointment = appointment,
                onView = {},
                onModify = {},
                onDelete = {}
            )
        }
    }
}