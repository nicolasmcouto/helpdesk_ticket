package tregu.helpdesk_ticket.Domain.dto

import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus

data class UpdateTicketRequest(
    val status: TicketStatus? = null,
    val priority: TicketPriority? = null,
    val assignedTo: String? = null
)
