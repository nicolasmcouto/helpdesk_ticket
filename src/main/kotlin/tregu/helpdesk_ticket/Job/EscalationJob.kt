package tregu.helpdesk_ticket.Job

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.Workflow.EscaleteWorkflow

@Component
class EscalationJob(
    private val ticketService: TicketService,
    private val escaleteWorkflow: EscaleteWorkflow
) {
    @Scheduled(fixedRate = 86400000)
    fun escaleteTicket() = runBlocking {
        val tickets = ticketService.findTicketsForEscalation()
        tickets.forEach{ ticket -> escaleteWorkflow.execute(ticket.id!!)}
    }
}