package tregu.helpdesk_ticket.Domain.dto

import java.time.OffsetDateTime

data class TagResponse(val id: Long, val name:String, var createdAt: OffsetDateTime?, var ticketsId: List<Long>?)
