package tregu.helpdesk_ticket.domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import tregu.helpdesk_ticket.domain.ticket.entity.CommentEntity

interface CommentRepository: JpaRepository<CommentEntity,Long> {

    fun findByTicketId(ticketId: Long): List<CommentEntity>
}