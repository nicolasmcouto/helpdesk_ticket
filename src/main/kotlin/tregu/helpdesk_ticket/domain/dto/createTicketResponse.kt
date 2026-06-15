package tregu.helpdesk_ticket.domain.dto

import jdk.jfr.Description
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus
import java.time.OffsetDateTime

data class createTicketResponse(
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