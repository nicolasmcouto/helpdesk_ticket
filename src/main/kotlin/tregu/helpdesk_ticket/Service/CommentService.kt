package tregu.helpdesk_ticket.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.Domain.Mapper.CommentMapper
import tregu.helpdesk_ticket.Domain.Repository.CommentRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.dto.CreateCommentRequest
import tregu.helpdesk_ticket.Domain.dto.CommentResponse
import tregu.helpdesk_ticket.Domain.ticket.entity.CommentEntity

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val ticketRepository: TicketRepository
) {

    suspend fun create(request: CreateCommentRequest): CommentResponse {
        return withContext(Dispatchers.IO) {

            val newComment = CommentEntity()
            newComment.ticket = ticketRepository.getReferenceById(request.ticketId)
            newComment.author = request.author
            newComment.content = request.content
            commentRepository.save(newComment)

            CommentMapper.toResponse(newComment)
        }

    }

    suspend fun findComment(ticketId: Long): List<CommentResponse> {
        return withContext(Dispatchers.IO) {

            val commentList = commentRepository.findByTicketId(ticketId)
            CommentMapper.toResponseList(commentList)
        }
    }

}