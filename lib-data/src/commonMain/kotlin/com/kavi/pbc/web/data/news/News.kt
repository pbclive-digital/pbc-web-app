package com.kavi.pbc.web.data.news

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
data class News(
    val id: String? = null,
    var title: String = "",
    var content: String = "",
    val newsStatus: NewsStatus = NewsStatus.DRAFT,
    var facebookLink: String? = null,
    var newsImage: String? = null,
    val createdTime: Long = 0,
    var publishedTime: Long = 0,
    var author: UserSummary = UserSummary()
) {
    fun getFormatCreatedDate(): String {
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

    fun getFormatPublishedDate(): String {
        val instant = fromEpochMilliseconds(publishedTime)
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
