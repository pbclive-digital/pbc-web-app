package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.appointment.data.model.SelectedType
import com.kavi.pbc.web.appointment.ui.common.AppointmentItem
import com.kavi.pbc.web.appointment.ui.common.AppointmentReqItem
import com.kavi.pbc.web.appointment.ui.common.CreateWithComponent
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
import pbcwebapp.ui_appointment.generated.resources.appointment_label_create_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_with

@Composable
fun AppointmentDashboardUI(navController: NavController) {

    val viewModel: AppointmentDashboardViewModel = viewModel { AppointmentDashboardViewModel() }

    val appointmentReqList by viewModel.appointmentReqList.collectAsState()
    val appointmentList by viewModel.appointmentList.collectAsState()

    val residentMonkList by viewModel.residentMonkList.collectAsState()

    var selectedType by remember { mutableStateOf(SelectedType.NONE) }
    var selectedAppointment by remember { mutableStateOf(Appointment(user = User(email = ""))) }
    var selectedAppointmentReq by remember { mutableStateOf(AppointmentRequest(user = User(email = ""))) }

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

        Row {
            when(UIUtil.screenType(maxWidth = maxWidth)) {
                ScreenType.PHONE -> {}
                else -> {
                    Column (
                        modifier = Modifier
                            .weight(.25f)
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
                                CreateWithComponent {
                                    // TODO: Create with any monk - open create dialog
                                }
                            }
                            items(residentMonkList) { monk ->
                                CreateWithComponent (user = monk) {
                                    // TODO: Create with selected monk - open create dialog
                                }
                            }
                        }
                    }

                    Column (
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
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
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
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
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

                    Column (
                        modifier = Modifier
                            .weight(.4f)
                            .height(maxHeight)
                            .padding(top = 10.dp, end = 15.dp)
                    ) {
                        when(selectedType) {
                            SelectedType.NONE -> {
                                // Nothing to do
                            }
                            SelectedType.APPOINTMENT_REQ -> SelectedAppointmentReq(selectedAppointmentReq)
                            SelectedType.APPOINTMENT -> SelectedAppointment(selectedAppointment)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedAppointment(selectedAppointment: Appointment) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp)
    ) {
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = selectedAppointment.title,
                fontFamily = PBCFontFamily,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun SelectedAppointmentReq(selectedAppointmentRequest: AppointmentRequest) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp)
    ) {
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = selectedAppointmentRequest.title,
                fontFamily = PBCFontFamily,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}