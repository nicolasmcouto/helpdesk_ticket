package tregu.helpdesk_ticket.domain.dto

import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus

data class UpdateTicketRequest(
    val status: TicketStatus? = null,
    val priority: TicketPriority? = null,
    val assignedTo: String? = null
)
