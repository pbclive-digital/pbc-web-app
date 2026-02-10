package com.kavi.pbc.web.data.appointment

import com.kavi.pbc.web.data.user.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant.Companion.fromEpochMilliseconds

@Serializable
data class Appointment(
    val id: String? = null,
    var title: String = "",
    val userId: String = "",
    val user: User,
    var selectedMonkId: String = "",
    var selectedMonk: User? = null,
    var date: Long = 0,
    var time: String = "",
    var reason: String = "",
    var appointmentStatus: AppointmentStatus = AppointmentStatus.PENDING
) {

    fun getFormatDate(): String {
        val instant = fromEpochMilliseconds(date)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val dateFormat = LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            day()
            char(',')
            char(' ')
            year()
        }
        return localDateTime.date.format(dateFormat)
    }
}
