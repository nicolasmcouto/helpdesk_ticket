package tregu.helpdesk_ticket.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult
import tregu.helpdesk_ticket.domain.Entity.TicketEntity
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus
import tregu.helpdesk_ticket.domain.Mapper.TicketMapper
import tregu.helpdesk_ticket.domain.Repository.TicketRepository
import tregu.helpdesk_ticket.domain.dto.TicketDetail
import tregu.helpdesk_ticket.domain.dto.UpdateTicketRequest
import tregu.helpdesk_ticket.domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.domain.dto.CreateTicketResponse

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
) {
    suspend fun create(
        request: CreateTicketRequest,
        author: String,
        classification: ClassificationResult
    ): CreateTicketResponse {
        val ticket = TicketEntity()
        ticket.title = request.title
        ticket.description = request.description
        ticket.createdBy = author
        ticket.priority = classification.priority
        ticket.category = classification.category

        val saved = withContext(Dispatchers.IO) { ticketRepository.save(ticket) }
        return TicketMapper.toResponse(saved)
    }

    suspend fun getTicketDetail(id: Long): TicketDetail? {
        return withContext(Dispatchers.IO) {
            val ticket = ticketRepository.findById(id).orElse(null)
            TicketMapper.toDetail(ticket)
        }
    }

    suspend fun findWithFilter(
        status: TicketStatus?,
        priority: TicketPriority?,
        category: String?,
        q: String?
    ): List<TicketDetail> {
        val response = withContext(Dispatchers.IO) {
            ticketRepository.findWithFilters(
                status = status?.name,
                priority = priority?.name,
                category = category,
                q = q
            )
        }
        return response.map { entity -> TicketMapper.toDetail(entity) }
    }

    suspend fun ticketUpdate(id: Long, update: UpdateTicketRequest): TicketDetail? {
            return withContext(Dispatchers.IO){
                val ticket = ticketRepository.findById(id).orElse(null)

                update.status?.let { ticket.status = it }
                update.priority?.let { ticket.priority = it  }
                update.assignedTo?.let { ticket.assignedTo = it }

                val saved = ticketRepository.save(ticket)

                TicketMapper.toDetail(saved)
            }
    }

}

