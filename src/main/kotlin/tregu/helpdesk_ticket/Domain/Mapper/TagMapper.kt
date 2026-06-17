package tregu.helpdesk_ticket.Domain.Mapper


import tregu.helpdesk_ticket.Domain.Entity.TagEntity
import tregu.helpdesk_ticket.Domain.dto.CreateTagResponse
import tregu.helpdesk_ticket.Domain.dto.InsertTagResponse

object TagMapper {
    fun toEntity(name: String): TagEntity {
        val tag = TagEntity()
        tag.name = name
        return tag
    }

    fun toCreateResponse(entity: TagEntity): CreateTagResponse {
        return CreateTagResponse(
            id = entity.id!!,
            name = entity.name
        )
    }

    fun toInsertTagResponse(entity: TagEntity): InsertTagResponse {
        return InsertTagResponse(
                id = entity.id!!,
                name = entity.name,
                ticket = entity.tickets,
                createdAt = entity.createdAt!!
                )
    }
}