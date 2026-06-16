package tregu.helpdesk_ticket.Controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tregu.helpdesk_ticket.Workflow.CreateTicketWorkflow
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus
import tregu.helpdesk_ticket.domain.dto.TicketDetail
import tregu.helpdesk_ticket.domain.dto.UpdateTicketRequest
import tregu.helpdesk_ticket.domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.domain.dto.CreateTicketResponse

@RestController
@RequestMapping("ticket")
class TicketController(
    private val ticketService: TicketService,
    private val ticketWorkflow: CreateTicketWorkflow
) {

    @PostMapping
    suspend fun createTicket(
        @RequestBody request: CreateTicketRequest, @RequestHeader("X-Author") author: String
    ): ResponseEntity<CreateTicketResponse> {
        val response = ticketWorkflow.execute(request, author)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    suspend fun ticketDetail(@PathVariable id: Long): ResponseEntity<TicketDetail> {

        val response = ticketService.getTicketDetail(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    suspend fun FindTicketFiltering(
        @RequestParam status: TicketStatus?,
        @RequestParam priority: TicketPriority?,
        @RequestParam category: String?,
        @RequestParam q: String?
    ): ResponseEntity<List<TicketDetail>> {

        val response = ticketService.findWithFilter(status, priority, category, q)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}")
    suspend fun ticketStateUpdate(
        @PathVariable id: Long,
        @RequestBody updateTicketRequest: UpdateTicketRequest
    ): ResponseEntity<TicketDetail> {

        val response = ticketService.ticketUpdate(id, updateTicketRequest)
        return ResponseEntity.ok(response)
    }
}