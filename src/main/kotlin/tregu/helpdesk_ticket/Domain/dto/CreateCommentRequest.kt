package tregu.helpdesk_ticket.Domain.dto

data class CreateCommentRequest(
   val ticketId: Long,
   val author: String,
   val content: String)
