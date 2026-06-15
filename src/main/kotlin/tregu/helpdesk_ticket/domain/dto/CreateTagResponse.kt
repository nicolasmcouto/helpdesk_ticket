package tregu.helpdesk_ticket.domain.dto

import java.time.OffsetDateTime

data class CreateTagResponse(val id: Long, val name:String, var createdAt: OffsetDateTime)
