package com.kavi.pbc.web.data.question

import com.kavi.pbc.web.data.user.UserSummary
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant.Companion.fromEpochMilliseconds

@Serializable
data class AnswerComment(
    val comment: String,
    val createdTime: Long,
    val author: UserSummary
) {
    fun getFormatDate(): String {
        val instant = fromEpochMilliseconds(createdTime)
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
