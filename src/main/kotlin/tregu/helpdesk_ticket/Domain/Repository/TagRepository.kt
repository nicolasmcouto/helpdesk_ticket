package tregu.helpdesk_ticket.Domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import tregu.helpdesk_ticket.Domain.Entity.TagEntity

interface TagRepository: JpaRepository<TagEntity, Long> {

    @Query("SELECT t FROM TagEntity t LEFT JOIN FETCH t.tickets WHERE t.id = :id")
    fun findByIdWithTickets(id: Long): TagEntity
}
