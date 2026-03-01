package com.kavi.pbc.web.data.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant.Companion.fromEpochMilliseconds

object DateTimeUtil {

    fun formatDate(dateTimeStamp: Long): String {
        val instant = fromEpochMilliseconds(dateTimeStamp)
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