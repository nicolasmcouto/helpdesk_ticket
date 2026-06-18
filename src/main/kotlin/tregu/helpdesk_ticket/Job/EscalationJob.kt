package tregu.helpdesk_ticket.Job

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.Workflow.EscalateWorkflow

@Component
class EscalationJob(
    private val ticketService: TicketService,
    private val escalateWorkflow: EscalateWorkflow
) {
    @Scheduled(fixedRate = 86400000)
    fun escalateTickets() = runBlocking {
        val tickets = ticketService.findTicketsForEscalation()
        tickets.forEach { ticket -> escalateWorkflow.execute(ticket.id!!) }
    }
}