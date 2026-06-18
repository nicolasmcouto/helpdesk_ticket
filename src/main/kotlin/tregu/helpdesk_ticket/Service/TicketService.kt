package tregu.helpdesk_ticket.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.Mapper.TicketMapper
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketStatsRepository
import tregu.helpdesk_ticket.Domain.dto.*
import java.math.BigDecimal
import java.time.OffsetDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val ticketStatsRepository: TicketStatsRepository
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
            val ticket = ticketRepository.findById(id).orElse(null) ?: return@withContext null
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
        return withContext(Dispatchers.IO) {
            val ticket = ticketRepository.findById(id).orElse(null) ?: return@withContext null

            update.status?.let { ticket.status = it }
            update.priority?.let { ticket.priority = it }
            update.assignedTo?.let { ticket.assignedTo = it }

            val saved = ticketRepository.save(ticket)

            TicketMapper.toDetail(saved)
        }
    }

    suspend fun getStats(): TicketStatsResponse {
        return withContext(Dispatchers.IO) {
            val basicStats = ticketStatsRepository.getBasicStats()

            val escalatedStats = ticketStatsRepository.getEscalatedStats()

            TicketStatsResponse(
                totalTickets = convertToLong(basicStats["totalTickets"]),
                openTickets = convertToLong(basicStats["openTickets"]),
                closedTickets = convertToLong(basicStats["closedTickets"]),
                totalEscalated = convertToLong(escalatedStats["totalEscalated"]),
                openEscalated = convertToLong(escalatedStats["openEscalated"]),
                ticketsByStatus = ticketStatsRepository.getTicketsByStatus(),
                ticketsByPriority = ticketStatsRepository.getTicketsByPriority(),
                ticketsByCategory = ticketStatsRepository.getTicketsByCategory()
            )
        }
    }

    private fun convertToLong(value: Any?): Long {
        return when (value) {
            is Long -> value
            is Int -> value.toLong()
            is BigDecimal -> value.toLong()
            is Number -> value.toLong()
            null -> 0L
            else -> value.toString().toLongOrNull() ?: 0L
        }
    }

    suspend fun findTicket(id: Long): TicketEntity {

        val ticket = ticketRepository.findById(id).getOrNull()
        return ticket!!
    }

    suspend fun escalate(entity: TicketEntity, summary: String): TicketDetail {
        entity.escalated = true
        entity.escalatedAt = OffsetDateTime.now()
        entity.escalationSummary = summary

        val saved = withContext(Dispatchers.IO) { ticketRepository.save(entity) }
        return TicketMapper.toDetail(saved)
    }

    suspend fun findTicketsForEscalation(): List<TicketEntity> {
        return withContext(Dispatchers.IO) {
            ticketRepository.findTicketsWithoutRecentComments()
        }
    }
}



