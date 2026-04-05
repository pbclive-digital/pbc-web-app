package com.kavi.pbc.web.data.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant.Companion.fromEpochMilliseconds

object DateTimeUtil {

    fun formatDate(dateTimeStamp: Long): String {
        val instant = fromEpochMilliseconds(dateTimeStamp)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
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

    fun datePickerInitializeMills(): Long {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return today
            .atStartOfDayIn(TimeZone.currentSystemDefault()) // Crucial: Use UTC here
            .toEpochMilliseconds()
    }

    fun datePickerInitializeMonthMills(): Long {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val monthAhead = today.plus(1, DateTimeUnit.MONTH)
        return monthAhead
            .atStartOfDayIn(TimeZone.currentSystemDefault()) // Crucial: Use UTC here
            .toEpochMilliseconds()
    }
}