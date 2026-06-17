package tregu.helpdesk_ticket.Domain.Mapper

import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.dto.TicketDetail
import tregu.helpdesk_ticket.Domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.Domain.dto.CreateTicketResponse

object TicketMapper {

    fun toEntity(
        request: CreateTicketRequest,
        author: String,
        priority: TicketPriority,
        category: String
    ): TicketEntity {
        val entity = TicketEntity()
        entity.title = request.title
        entity.description = request.description
        entity.createdBy = author
        entity.priority = priority
        entity.category = category
        return entity
    }

    fun toResponse(entity: TicketEntity): CreateTicketResponse {
        return CreateTicketResponse(
            id = entity.id!!,
            title = entity.title,
            description = entity.description,
            status = entity.status,
            priority = entity.priority ?: TicketPriority.MEDIUM,
            category = entity.category ?: "uncategorized",
            createBy = entity.createdBy,
            assignedTo = entity.assignedTo,
            escalated = entity.escalated,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!
        )
    }

    fun toDetail(entity: TicketEntity): TicketDetail {
        return TicketDetail(
            id = entity.id!!,
            title = entity.title,
            description = entity.description,
            status = entity.status,
            priority = entity.priority!!,
            category = entity.category,
            createdBy = entity.createdBy,
            assignedTo = entity.assignedTo,
            escalated = entity.escalated,
            escalatedAt = entity.escalatedAt,
            escalationSummary = entity.escalationSummary,
            tags = entity.tags.toList(),
            comments = entity.comments.toList(),
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!,
            resolvedAt = entity.resolvedAt
        )
    }
}