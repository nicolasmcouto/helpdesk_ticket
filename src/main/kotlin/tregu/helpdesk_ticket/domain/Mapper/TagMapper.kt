package tregu.helpdesk_ticket.domain.Mapper

import tregu.helpdesk_ticket.domain.Entity.TagEntity
import tregu.helpdesk_ticket.domain.dto.TagResponse

object TagMapper {
    fun toResponse(entity: TagEntity): TagResponse {
        return TagResponse(
            id = entity.id!!,
            name = entity.name,
            createdAt = entity.createdAt,
            ticketsId = entity.tickets.map { it.id }
        )
    }

    fun toResponseList(entities: List<TagEntity>): List<TagResponse> {
        return entities.map { toResponse(it) }
    }
}