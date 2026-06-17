package tregu.helpdesk_ticket.Domain.dto


data class TicketStatsResponse(
    val totalTickets: Long,
    val openTickets: Long,
    val closedTickets: Long,
    val totalEscalated: Long,
    val openEscalated: Long,
    val ticketsByStatus: Map<String, Long>,
    val ticketsByPriority: Map<String, Long>,
    val ticketsByCategory: Map<String, Long>
)
