package tregu.helpdesk_ticket.Service


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.Domain.Entity.TagEntity
import tregu.helpdesk_ticket.Domain.Mapper.TagMapper
import tregu.helpdesk_ticket.Domain.Repository.TagRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.dto.CreateTagResponse
import tregu.helpdesk_ticket.Domain.dto.InsertTagResponse
import kotlin.jvm.optionals.getOrNull

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val ticketRepository: TicketRepository
) {

    suspend fun createNewTag(name: String): CreateTagResponse {
        return withContext(Dispatchers.IO) {
            val newTag = TagEntity()
            newTag.name = name
            val saved = tagRepository.save(newTag)
            TagMapper.toCreateResponse(saved)
        }
    }

    suspend fun insertTag(id: Long, ticketId: Long): InsertTagResponse {
        return withContext(Dispatchers.IO) {
            val tag = tagRepository.findByIdWithTickets(id)
            val ticket = ticketRepository.findById(ticketId).getOrNull()
            tag.tickets.add(ticket!!)
            tagRepository.save(tag)
            TagMapper.toInsertTagResponse(tag)
        }
    }

    suspend fun removeTag(id: Long, ticketId: Long) {
        withContext(Dispatchers.IO) {
            val tag = tagRepository.findByIdWithTickets(id)
            val ticket = ticketRepository.getReferenceById(ticketId)
            tag.tickets.remove(ticket)
            tagRepository.save(tag)
        }
    }
}