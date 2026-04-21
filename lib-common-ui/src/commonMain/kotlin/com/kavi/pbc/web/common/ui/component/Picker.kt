package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.common_date_picker_cancel
import pbcwebapp.lib_common_ui.generated.resources.common_date_picker_pick
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    showDatePicker: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onConfirmAction: () -> Unit,
    onDismissAction: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = { showDatePicker.value = false },
        confirmButton = {
            TextButton(onClick = {
                // Handle selected date from datePickerState.selectedDateMillis
                onConfirmAction.invoke()
            }) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = stringResource(Res.string.common_date_picker_pick),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissAction.invoke() }) {
                Text(
                    text = stringResource(Res.string.common_date_picker_cancel),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) {
        DatePicker(state = datePickerState, showModeToggle = false, title = null, headline = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog (
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    // 1. Get the current moment in time
    val now = Clock.System.now()

    // 2. Convert it to the system's local time zone
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    val initialHour = localDateTime.hour
    val initialMinute = localDateTime.minute

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true // Set to true for 24-hour format
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)
                onDismiss()
            }) {
                Text(
                    text = stringResource(Res.string.common_date_picker_pick),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(Res.string.common_date_picker_cancel),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}