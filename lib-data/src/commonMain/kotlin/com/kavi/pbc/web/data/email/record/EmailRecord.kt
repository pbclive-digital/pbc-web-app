package com.kavi.pbc.web.data.email.record

import com.kavi.pbc.live.data.model.broadcast.EmailTemplateType
import com.kavi.pbc.web.data.email.EmailGroupHeading
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant.Companion.fromEpochMilliseconds

@Serializable
data class EmailRecord(
    val id: String = "",
    val emailTemplate: EmailTemplateType,
    val sentTime: Long = 0,
    val emailGroupHeadings: List<EmailGroupHeading> = listOf(),
    val emailRecordContent: EmailRecordContent
) {
    fun getFormatSentDate(): String {
        val instant = fromEpochMilliseconds(sentTime)
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