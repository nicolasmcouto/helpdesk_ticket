package tregu.helpdesk_ticket.Workflow

import org.springframework.stereotype.Component
import tregu.helpdesk_ticket.Service.LlmService
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.Domain.dto.TicketDetail

@Component
class EscaleteWorkflow(
    private val llmService: LlmService,
    private val ticketService: TicketService
) {
    suspend fun execute(ticketId: Long): TicketDetail{
       val ticket =  ticketService.findTicket(ticketId)
        val summarizeMessage = llmService.summarize(ticket.title, ticket.description, ticket.status, ticket.createdAt!!)
        return ticketService.escalete(ticket, summarizeMessage)

    }
}