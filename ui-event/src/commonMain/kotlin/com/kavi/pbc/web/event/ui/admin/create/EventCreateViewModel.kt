package com.kavi.pbc.web.event.ui.admin.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.EventRecurringDay
import com.kavi.pbc.web.data.event.EventType
import com.kavi.pbc.web.data.event.VenueType
import com.kavi.pbc.web.data.event.potluck.PotluckItem
import com.kavi.pbc.web.data.event.signup.SignUpSheet
import com.kavi.pbc.web.data.util.DateTimeUtil
import com.kavi.pbc.web.event.data.model.EventCreateOrModifyUiState
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf
import kotlin.time.Clock

class EventCreateViewModel: ViewModel() {

    val eventRemoteRepository = EventRemoteRepository()
    private val _eventCreationOrModifyState = MutableStateFlow(EventCreateOrModifyUiState.NONE)
    val eventCreationOrModifyState: StateFlow<EventCreateOrModifyUiState> = _eventCreationOrModifyState

    private val _eventFormValidationError = MutableStateFlow("")
    val eventFormValidationError: StateFlow<String> = _eventFormValidationError

    private val _createOrModifyEvent = MutableStateFlow(Event(
        creator = Session.user!!.id!!,
        createdTime = Clock.System.now().toEpochMilliseconds(),
    ))
    val createOrModifyEvent: StateFlow<Event> = _createOrModifyEvent

    private var _potluckItemList = MutableStateFlow<MutableList<PotluckItem>>(mutableListOf())
    val potluckItemList: StateFlow<List<PotluckItem>> = _potluckItemList

    private var _signUpSheetItemList = MutableStateFlow<MutableList<SignUpSheet>>(mutableListOf())
    val signUpSheetItemList: StateFlow<List<SignUpSheet>> = _signUpSheetItemList

    private var eventImageFile: PlatformFile? = null

    fun initiateNewEvent() {
        _createOrModifyEvent.value = Event(
            creator = Session.user!!.id!!,
            createdTime = Clock.System.now().toEpochMilliseconds(),
        )

        // clearing the potluck list
        _potluckItemList.value.clear()

        // clearing the sign-up sheet list
        _signUpSheetItemList.value.clear()
    }

    fun setModifyEvent(event: Event) {
        _createOrModifyEvent.value = event

        // Assign potluck items if that available
        if(event.potluckAvailable) {
            event.potluckItemList?.let {
                _potluckItemList.value = it
            }
        }

        // Assign sign-up sheets if that available
        if (event.signUpSheetAvailable) {
            event.signUpSheetList?.let {
                _signUpSheetItemList.value = it
            }
        }
    }

    fun revokeEventCreateOrModifyUiState() {
        _eventFormValidationError.value = ""
        _eventCreationOrModifyState.value = EventCreateOrModifyUiState.NONE
    }

    fun getInitialEventType(): String {
        return if (_createOrModifyEvent.value.eventType == EventType.DEFAULT)
            ""
        else
            _createOrModifyEvent.value.eventType.name
    }

    fun getInitialEventRecurringDay(): String {
        return if (_createOrModifyEvent.value.recurringDay == EventRecurringDay.NONE)
            ""
        else
            _createOrModifyEvent.value.recurringDay.name
    }

    fun getInitialEventDate(): String {
        return if (_createOrModifyEvent.value.eventDate.toInt() == 0)
            "SELECT DATE"
        else
            _createOrModifyEvent.value.getFormatDate()
    }

    fun getInitialStartTime(): String {
        return _createOrModifyEvent.value.startTime.ifEmpty {
            "FROM"
        }
    }

    fun getInitialEndTime(): String {
        return _createOrModifyEvent.value.endTime.ifEmpty {
            "TO"
        }
    }

    fun getInitialVenueType(): String {
        return if (_createOrModifyEvent.value.venueType == VenueType.DEFAULT)
            "VENUE TYPE"
        else
            _createOrModifyEvent.value.venueType.name
    }

    fun formatDate(selectedDateMils: Long?): Pair<String, Long> {
        return selectedDateMils?.let {
            DateTimeUtil.formatDate(it)
        }?: run {
            Pair("Select Event Date".uppercase(), 0L)
        }
    }

    fun formatTime(hour: Int, minute: Int): String {
        // Ensure the values stay within bounds (Optional safety check)
        val h = hour.coerceIn(0, 23)
        val m = minute.coerceIn(0, 59)

        // padStart ensures there are always 2 digits, using '0' as a filler
        val hourString = h.toString().padStart(2, '0')
        val minuteString = m.toString().padStart(2, '0')

        val value = "$hourString:$minuteString"
        val label = if (hour >= 12) "$value PM" else "$value AM"

        return label
    }

    fun updateName(name: String) {
        _createOrModifyEvent.value.name = name
    }

    fun updateDescription(description: String) {
        _createOrModifyEvent.value.description = description
    }

    fun updateEventType(eventType: String) {
        when(eventType) {
            EventType.SPECIAL.name -> _createOrModifyEvent.value.eventType = EventType.SPECIAL
            EventType.BUDDHISM_CLASS.name -> _createOrModifyEvent.value.eventType = EventType.BUDDHISM_CLASS
            EventType.MEDITATION.name -> _createOrModifyEvent.value.eventType = EventType.MEDITATION
            EventType.DHAMMA_TALK.name -> _createOrModifyEvent.value.eventType = EventType.DHAMMA_TALK
            EventType.RECURRING.name -> _createOrModifyEvent.value.eventType = EventType.RECURRING
        }
    }

    fun updateEventRecurringDay(eventRecurringDay: String) {
        when(eventRecurringDay) {
            EventRecurringDay.MONDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.MONDAY
            EventRecurringDay.TUESDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.TUESDAY
            EventRecurringDay.WEDNESDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.WEDNESDAY
            EventRecurringDay.THURSDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.THURSDAY
            EventRecurringDay.FRIDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.FRIDAY
            EventRecurringDay.SATURDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.SATURDAY
            EventRecurringDay.SUNDAY.name -> _createOrModifyEvent.value.recurringDay = EventRecurringDay.SUNDAY
        }
    }

    fun updateVenueType(venueType: String) {
        when(venueType) {
            VenueType.PHYSICAL.name -> _createOrModifyEvent.value.venueType = VenueType.PHYSICAL
            VenueType.VIRTUAL.name -> _createOrModifyEvent.value.venueType = VenueType.VIRTUAL
        }
    }

    fun updateVenue(venue: String) {
        _createOrModifyEvent.value.venue = venue
    }

    fun updateVenueAddress(venueAddress: String) {
        _createOrModifyEvent.value.venueAddress = venueAddress
    }

    fun updateMeetingUrl(meetingUrl: String) {
        _createOrModifyEvent.value.meetingUrl = meetingUrl
    }

    fun updateDate(date: Long?) {
        date?.let {
            _createOrModifyEvent.value.eventDate = it
        }
    }

    fun updateStartTime(startTime: String) {
        _createOrModifyEvent.value.startTime = startTime
    }

    fun updateEndTime(endTime: String) {
        _createOrModifyEvent.value.endTime = endTime
    }

    fun updateRegistrationRequiredFlag(isRegistrationRequired: Boolean) {
        _createOrModifyEvent.value.registrationRequired = isRegistrationRequired
    }

    fun updateSeatCount(seatCount: Int) {
        _createOrModifyEvent.value.openSeatCount = seatCount
    }

    fun updatePotluckAvailabilityFlag(isPotluckAvailable: Boolean) {
        _createOrModifyEvent.value.potluckAvailable = isPotluckAvailable
    }

    fun addPotluckItem(potluckItem: PotluckItem) {
        _potluckItemList.update { currentList ->
            (currentList + potluckItem) as MutableList<PotluckItem>
        }
        _createOrModifyEvent.value.potluckItemList = _potluckItemList.value
    }

    fun removePotluckItem(potluckItem: PotluckItem) {
        _potluckItemList.value = _potluckItemList.value
            .filterNot { it == potluckItem }
            .toMutableList()
        _createOrModifyEvent.value.potluckItemList = _potluckItemList.value
    }

    fun updateSignUpAvailabilityFlag(isSignUpSheetAvailable: Boolean) {
        _createOrModifyEvent.value.signUpSheetAvailable = isSignUpSheetAvailable
    }

    fun addSignUpSheet(signUpSheet: SignUpSheet) {
        _signUpSheetItemList.update { currentList ->
            (currentList + signUpSheet) as MutableList<SignUpSheet>
        }
        _createOrModifyEvent.value.signUpSheetList = _signUpSheetItemList.value
    }

    fun removeSignUpSheet(signUpSheet: SignUpSheet) {
        _signUpSheetItemList.value = _signUpSheetItemList.value
            .filterNot { it == signUpSheet }
            .toMutableList()
        _createOrModifyEvent.value.signUpSheetList = _signUpSheetItemList.value
    }

    fun updateEventImageFile(newsImage: PlatformFile) {
        eventImageFile = newsImage
    }

    fun uploadEventImageAndCreateOrUpdateEvent(isModify: Boolean = false) {
        val validationFormResult = isValidNewsForm()
        if (validationFormResult.first) {
            val formatedEventName = _createOrModifyEvent.value.name
                .replace(" ", "_")
                .replace("-", "_")

            if (eventImageFile != null) {
                viewModelScope.launch {
                    when(val response = eventRemoteRepository.uploadEventImage(formatedEventName, eventImageFile!!)){
                        is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError -> {
                            // Do nothing for now
                        }
                        is ResultWrapper.HttpError -> {
                            if (isModify) {
                                updateEvent()
                            } else {
                                createEvent()
                            }
                        }
                        is ResultWrapper.Success -> {
                            response.value.body?.let {
                                _createOrModifyEvent.value.eventImage = it
                                if (isModify) {
                                    updateEvent()
                                } else {
                                    createEvent()
                                }
                            }
                        }
                    }
                }
            } else {
                if (isModify) {
                    updateEvent()
                } else {
                    createEvent()
                }
            }
        } else {
            _eventFormValidationError.value = validationFormResult.second
            _eventCreationOrModifyState.value = EventCreateOrModifyUiState.EMPTY_FIELD
        }
    }

    private fun isValidNewsForm(): Pair<Boolean, String> {
        if (_createOrModifyEvent.value.name.isEmpty()) {
            return Pair(false, "Event Name is empty")
        }

        if (_createOrModifyEvent.value.description.isEmpty()) {
            return Pair(false, "Event Description is empty")
        }

        if (_createOrModifyEvent.value.eventType == EventType.DEFAULT) {
            return Pair(false, "Event Type not selected")
        }

        if (_createOrModifyEvent.value.eventType == EventType.RECURRING) {
            if (_createOrModifyEvent.value.recurringDay == EventRecurringDay.NONE) {
                return Pair(false, "Event recurring day is not selected")
            }
        } else {
            if (_createOrModifyEvent.value.eventDate == 0L) {
                return Pair(false, "Event Date not selected")
            }
        }

        if (_createOrModifyEvent.value.startTime.isEmpty()) {
            return Pair(false, "Event Start Time not selected")
        }

        if (_createOrModifyEvent.value.endTime.isEmpty()) {
            return Pair(false, "Event End Time not selected")
        }

        if (_createOrModifyEvent.value.venueType == VenueType.DEFAULT) {
            return Pair(false, "Event VenueType not selected")
        }

        return Pair(true, "")
    }

    private fun createEvent() {
        viewModelScope.launch {
            if (_createOrModifyEvent.value.eventType == EventType.RECURRING) {
                _createOrModifyEvent.value.eventDate = 0L
            } else {
                _createOrModifyEvent.value.recurringDay = EventRecurringDay.NONE
            }

            when(val response = eventRemoteRepository.createEvent(event = _createOrModifyEvent.value)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _eventCreationOrModifyState.value = EventCreateOrModifyUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventCreationOrModifyState.value = EventCreateOrModifyUiState.SUCCESS
                        eventImageFile = null
                    }
                }
            }
        }
    }

    private fun updateEvent() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.updateEvent(eventId = _createOrModifyEvent.value.id!!, event = _createOrModifyEvent.value)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _eventCreationOrModifyState.value = EventCreateOrModifyUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventCreationOrModifyState.value = EventCreateOrModifyUiState.SUCCESS
                        eventImageFile = null
                    }
                }
            }
        }
    }
}