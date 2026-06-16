package tregu.helpdesk_ticket.Workflow.Dto

import tregu.helpdesk_ticket.domain.Enum.TicketPriority

data class ClassificationResult(
    val priority: TicketPriority,
    val category: String
)
