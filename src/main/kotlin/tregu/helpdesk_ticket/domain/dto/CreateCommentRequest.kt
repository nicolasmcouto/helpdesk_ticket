package tregu.helpdesk_ticket.domain.dto

data class CreateCommentRequest(
   val ticketId: Long,
   val author: String,
   val content: String)
