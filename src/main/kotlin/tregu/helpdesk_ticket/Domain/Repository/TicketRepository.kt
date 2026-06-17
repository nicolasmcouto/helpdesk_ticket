package tregu.helpdesk_ticket.Domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity

@Repository
interface TicketRepository : JpaRepository<TicketEntity, Long> {

    @Query(
        """
    SELECT * FROM helpdesk.ticket 
    WHERE (:status IS NULL OR status = :status)
    AND (:priority IS NULL OR priority = :priority)
    AND (:category IS NULL OR category = :category)
    AND (:q IS NULL OR title LIKE %:q% OR description LIKE %:q%)
""", nativeQuery = true
    )
    fun findWithFilters(
        @Param("status") status: String?,
        @Param("priority") priority: String?,
        @Param("category") category: String?,
        @Param("q") q: String?
    ): List<TicketEntity>

    @Query("""
    SELECT t.* FROM helpdesk.ticket t
    WHERE t.status IN ('OPEN', 'IN_PROGRESS')
    AND t.escalated = false
    AND NOT EXISTS (
        SELECT 1 FROM helpdesk.comment c
        WHERE c.ticket_id = t.id
        AND c.created_at > NOW() - INTERVAL 24 HOUR
    )
""", nativeQuery = true)
    fun findTicketsWithoutRecentComments(): List<TicketEntity>
}