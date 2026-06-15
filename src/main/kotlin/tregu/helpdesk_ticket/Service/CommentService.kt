package tregu.helpdesk_ticket.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.domain.Mapper.CommentMapper
import tregu.helpdesk_ticket.domain.Repository.CommentRepository
import tregu.helpdesk_ticket.domain.Repository.TicketRepository
import tregu.helpdesk_ticket.domain.dto.CreateCommentRequest
import tregu.helpdesk_ticket.domain.dto.CommentResponse
import tregu.helpdesk_ticket.domain.ticket.entity.CommentEntity
import kotlin.jvm.optionals.getOrNull

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val ticketRepository: TicketRepository
)   {

    suspend fun create(request: CreateCommentRequest): CommentResponse{
        return withContext(Dispatchers.IO) {
            val ticket = ticketRepository.findById(request.ticketId).getOrNull()
            val newComment = CommentEntity()
            newComment.ticket = ticket
            newComment.author = request.author
            newComment.content = request.content

            commentRepository.save(newComment)

            CommentMapper.toResponse(newComment)
        }

}
    suspend fun findComment(ticketId:Long): List<CommentResponse>{
            return withContext(Dispatchers.IO){
                val commentList = commentRepository.findByTicketId(ticketId)
                CommentMapper.toResponseList(commentList)
            }
    }

}