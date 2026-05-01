package com.kavi.pbc.web.local.events

import com.kavi.pbc.web.local.events.event.PBCAppEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object PBCEventBus {
    private val _events = MutableSharedFlow<PBCAppEvent>(
        extraBufferCapacity = 1 // Prevents blocking the sender
    )
    val events = _events.asSharedFlow()

    suspend fun publish(event: PBCAppEvent) {
        _events.emit(event)
    }
}