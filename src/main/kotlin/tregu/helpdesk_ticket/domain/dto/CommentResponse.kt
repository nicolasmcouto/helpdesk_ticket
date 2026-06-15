package tregu.helpdesk_ticket.domain.dto

import tregu.helpdesk_ticket.domain.Entity.TicketEntity
import java.time.OffsetDateTime

data class CommentResponse(
    val id: Long,
    val ticketId: Long,
    val author: String,
    val content: String,
    val createdAt: OffsetDateTime
) {

}
