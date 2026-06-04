package com.kavi.pbc.web.data.email.record

import com.kavi.pbc.live.data.model.broadcast.EmailTemplateType
import com.kavi.pbc.web.data.email.EmailGroupHeading
import kotlinx.serialization.Serializable

@Serializable
data class EmailRecord(
    val id: String = "",
    val emailTemplate: EmailTemplateType,
    val sentTime: Long = 0,
    val emailGroupHeadings: List<EmailGroupHeading> = listOf(),
    val emailRecordContent: EmailRecordContent
)