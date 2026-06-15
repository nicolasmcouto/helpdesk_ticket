package tregu.helpdesk_ticket.domain.dto

import tregu.helpdesk_ticket.domain.Entity.TagEntity
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus
import tregu.helpdesk_ticket.domain.ticket.entity.CommentEntity
import java.time.OffsetDateTime

data class TicketDetail(
    val id: Long,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val priority: TicketPriority,
    val category: String?,
    val createdBy: String,
    val assignedTo: String?,
    val escalated: Boolean,
    val escalatedAt: OffsetDateTime?,
    val escalationSummary: String?,
    val tags: List<TagEntity>,
    val comments: List<CommentEntity>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val resolvedAt: OffsetDateTime?,
)
