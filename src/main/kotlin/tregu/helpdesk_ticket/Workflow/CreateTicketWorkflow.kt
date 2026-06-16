package tregu.helpdesk_ticket.Workflow


import org.springframework.stereotype.Component
import tregu.helpdesk_ticket.Service.LlmService
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.domain.dto.CreateTicketResponse



@Component
class CreateTicketWorkflow(
    private val llmService: LlmService,
    private val ticketService: TicketService,
) {
      suspend fun execute(request: CreateTicketRequest, author: String): CreateTicketResponse {
        val classification = llmService.classify(request.title, request.description)
        return ticketService.create(request, author, classification)
    }


}