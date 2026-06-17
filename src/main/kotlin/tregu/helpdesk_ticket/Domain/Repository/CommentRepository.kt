package tregu.helpdesk_ticket.Domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import tregu.helpdesk_ticket.Domain.ticket.entity.CommentEntity

interface CommentRepository : JpaRepository<CommentEntity, Long> {

    fun findByTicketId(ticketId: Long): List<CommentEntity>

}