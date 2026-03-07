package com.kavi.pbc.web.appointment.ui.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.appointment.ui.dashboard.SelectedAppointment
import com.kavi.pbc.web.appointment.ui.dashboard.SelectedAppointmentReq
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedAppointmentReqBottomSheetUI(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    selectedAppointmentReq: AppointmentRequest
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        SelectedAppointmentReq(selectedAppointmentRequest = selectedAppointmentReq)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedAppointmentBottomSheetUI(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    selectedAppointment: Appointment
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        SelectedAppointment(selectedAppointment = selectedAppointment)
    }
}