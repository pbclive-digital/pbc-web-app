package com.kavi.pbc.web.data.event

import com.kavi.pbc.web.data.event.potluck.PotluckItem
import com.kavi.pbc.web.data.event.signup.SignUpSheet
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String? = null,
    var name: String = "",
    var description: String = "",
    var eventStatus: EventStatus = EventStatus.DRAFT,
    var eventDate: Long = 0,
    var startTime: String = "",
    var endTime: String = "",
    val createdTime: Long = Clock.System.now().toEpochMilliseconds(),
    var venueType: VenueType = VenueType.DEFAULT,
    var venue: String? = null,
    var venueAddress: String? = null,
    var meetingUrl: String? = null,
    val creator: String,
    var eventImage: String? = null,
    var eventType: EventType = EventType.DEFAULT,
    var registrationRequired: Boolean = false,
    var openSeatCount: Int? = null,
    var potluckAvailable: Boolean = false,
    var potluckItemList: MutableList<PotluckItem>? = mutableListOf(),
    var signUpSheetAvailable: Boolean = false,
    var signUpSheetList: MutableList<SignUpSheet>? = mutableListOf()
) {
    fun getFormatDate(): String {
        val instant = Instant.fromEpochMilliseconds(eventDate)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.year} - ${localDateTime.month} - ${localDateTime.dayOfMonth}"
    }

    fun getPlace(): String {
        return when(venueType) {
            VenueType.DEFAULT -> { "" }
            VenueType.VIRTUAL -> "Online"
            VenueType.PHYSICAL -> {
               venue ?: run { "PBC" }
            }
        }
    }
}
