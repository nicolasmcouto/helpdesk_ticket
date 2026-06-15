package tregu.helpdesk_ticket.domain.Repository

import org.springframework.data.jpa.repository.JpaRepository
import tregu.helpdesk_ticket.domain.Entity.TagEntity

interface TagRepository: JpaRepository<TagEntity, Long> {
}