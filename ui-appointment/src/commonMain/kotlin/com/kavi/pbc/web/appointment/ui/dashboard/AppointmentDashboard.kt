package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.appointment.data.model.SelectedType
import com.kavi.pbc.web.appointment.ui.common.AppointmentItem
import com.kavi.pbc.web.appointment.ui.common.AppointmentReqItem
import com.kavi.pbc.web.appointment.ui.common.WithComponent
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_label_accepted
import pbcwebapp.ui_appointment.generated.resources.appointment_label_accepted_phone
import pbcwebapp.ui_appointment.generated.resources.appointment_label_create_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_details
import pbcwebapp.ui_appointment.generated.resources.appointment_label_empty_selection
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_phone
import pbcwebapp.ui_appointment.generated.resources.appointment_label_with

@Composable
fun AppointmentDashboardUI(navController: NavController) {

    val viewModel: AppointmentDashboardViewModel = viewModel { AppointmentDashboardViewModel() }

    LaunchedEffect(Unit) {
        viewModel.checkAppointmentReqCreateEligibility()
        viewModel.fetchResidentMonkList()
        viewModel.fetchAppointmentRequests()
        viewModel.fetchAppointments()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        when(UIUtil.screenType(maxWidth = maxWidth)) {
            ScreenType.PHONE -> PhoneContent(screenWidth = maxWidth, screenHeight = maxHeight, viewModel = viewModel)
            else -> WebContent(maxHeight = maxHeight, viewModel = viewModel)
        }
    }
}

@Composable
private fun PhoneContent(screenWidth: Dp, screenHeight: Dp, viewModel: AppointmentDashboardViewModel) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    var selectedPagerIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberPagerState { 2 }

    LaunchedEffect(selectedPagerIndex) {
        state.animateScrollToPage(selectedPagerIndex)
    }

    Column {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppointmentRequestList(screenHeight: Dp, viewModel: AppointmentDashboardViewModel) {
    val appointmentRequestList by viewModel.appointmentReqList.collectAsState()

    val viewUserSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showViewSheet = remember { mutableStateOf(false) }
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

                },
                onDelete = {

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

@Composable
private fun WebContent(maxHeight: Dp, viewModel: AppointmentDashboardViewModel) {
    val appointmentReqList by viewModel.appointmentReqList.collectAsState()
    val appointmentList by viewModel.appointmentList.collectAsState()
    val residentMonkList by viewModel.residentMonkList.collectAsState()

    var selectedType by remember { mutableStateOf(SelectedType.NONE) }
    var selectedAppointment by remember { mutableStateOf(Appointment(user = User(email = ""))) }
    var selectedAppointmentReq by remember { mutableStateOf(AppointmentRequest(user = User(email = ""))) }

    Row {
        Column(
            modifier = Modifier
                .weight(.2f)
                .height(maxHeight)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_create_request),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_with),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            LazyColumn {
                item {
                    WithComponent {
                        // TODO: Create with any monk - open create dialog
                    }
                }
                items(residentMonkList) { monk ->
                    WithComponent(user = monk) {
                        // TODO: Create with selected monk - open create dialog
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(.35f)
                .height(maxHeight)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            LazyColumn {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.appointment_label_request),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )
                }
                items(appointmentReqList) { appointmentReq ->
                    AppointmentReqItem(appointmentReq = appointmentReq, onView = {
                        selectedType = SelectedType.APPOINTMENT_REQ
                        selectedAppointmentReq = appointmentReq
                    }, onAccept = {}, onDelete = {}, onModify = {})
                }

                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        text = stringResource(Res.string.appointment_label_accepted),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )
                }

                items(appointmentList) { appointment ->
                    AppointmentItem(appointment = appointment, onView = {
                        selectedType = SelectedType.APPOINTMENT
                        selectedAppointment = appointment
                    }, onDelete = {}, onModify = {})
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(.45f)
                .height(maxHeight)
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_details),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedType) {
                SelectedType.NONE -> EmptySelection()
                SelectedType.APPOINTMENT_REQ -> SelectedAppointmentReq(selectedAppointmentReq)
                SelectedType.APPOINTMENT -> SelectedAppointment(selectedAppointment)
            }
        }
    }
}

@Composable
private fun EmptySelection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.appointment_label_empty_selection),
                fontFamily = PBCFontFamily,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SelectedAppointment(selectedAppointment: Appointment) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row {
            Column (modifier = Modifier.weight(.25f)) {
                selectedAppointment.selectedMonk?.let {
                    WithComponent(user = it)
                }?: run {
                    WithComponent()
                }
            }
            Column (
                modifier = Modifier
                    .weight(.75f)
                    .padding(start = 8.dp, top = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = selectedAppointment.title,
                    fontFamily = PBCFontFamily,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = selectedAppointment.reason,
                    fontFamily = PBCFontFamily,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "on ${selectedAppointment.getFormatDate()} at ${selectedAppointment.time}",
                    fontFamily = PBCFontFamily,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SelectedAppointmentReq(selectedAppointmentRequest: AppointmentRequest) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row {
            Column (modifier = Modifier.weight(.25f)) {
                selectedAppointmentRequest.selectedMonk?.let {
                    WithComponent(user = it)
                }?: run {
                    WithComponent()
                }
            }
            Column (
                modifier = Modifier
                    .weight(.75f)
                    .padding(start = 8.dp, top = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = selectedAppointmentRequest.title,
                    fontFamily = PBCFontFamily,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = selectedAppointmentRequest.reason,
                    fontFamily = PBCFontFamily,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}