package tregu.helpdesk_ticket.Domain.dto

import java.time.OffsetDateTime

data class CommentResponse(
    val id: Long,
    val ticketId: Long,
    val author: String,
    val content: String,
    val createdAt: OffsetDateTime
) {

}
