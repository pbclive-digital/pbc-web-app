package com.kavi.pbc.web.data.email.send

import com.kavi.pbc.web.data.email.EmailGroupHeading
import kotlinx.serialization.Serializable

@Serializable
data class EmailBroadcastMsg(
    var subject: String = "",
    var title: String = "",
    var message: String = ""
)

@Serializable
data class EmailBroadcastRequest(
    val emailBroadcastMessage: EmailBroadcastMsg,
    var emailGroupHeadings: List<EmailGroupHeading>
)
