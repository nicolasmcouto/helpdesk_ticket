package tregu.helpdesk_ticket.Domain.dto

import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import java.time.OffsetDateTime

data class CreateTicketResponse(
    val id: Long,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val priority: TicketPriority,
    val category: String,
    val createBy: String?,
    val assignedTo: String?,
    val escalated: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)