package tregu.helpdesk_ticket.domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tregu.helpdesk_ticket.domain.Entity.TicketEntity

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
}