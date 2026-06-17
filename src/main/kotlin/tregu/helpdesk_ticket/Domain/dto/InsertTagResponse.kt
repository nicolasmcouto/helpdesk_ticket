package tregu.helpdesk_ticket.Domain.dto

import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import java.time.OffsetDateTime

data class InsertTagResponse(val id: Long, val name: String, val createdAt: OffsetDateTime,val ticket: MutableSet<TicketEntity>)
