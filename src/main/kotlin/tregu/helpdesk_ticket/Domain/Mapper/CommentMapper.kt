package tregu.helpdesk_ticket.Domain.Mapper

import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.dto.CommentResponse
import tregu.helpdesk_ticket.Domain.dto.CreateCommentRequest
import tregu.helpdesk_ticket.Domain.ticket.entity.CommentEntity

object CommentMapper {
    fun toEntity(request: CreateCommentRequest, author: String, ticketId: TicketEntity): CommentEntity {
        val comment = CommentEntity()
        comment.content = request.content
        comment.author = author
        comment.ticket = ticketId
        return comment
    }

    fun toResponse(entity: CommentEntity): CommentResponse {
        return CommentResponse(
            id = entity.id!!,
            content = entity.content,
            ticketId = entity.ticket?.id!!,
            author = entity.author,
            createdAt = entity.createdAt!!
        )
    }

    fun toResponseList(entities: List<CommentEntity>): List<CommentResponse> {
        return entities.map { toResponse(it) }
    }
}