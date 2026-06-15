package tregu.helpdesk_ticket.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.context.properties.PropertyMapper.Source.Always.Mapper
import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.domain.Entity.TagEntity
import tregu.helpdesk_ticket.domain.Mapper.TagMapper
import tregu.helpdesk_ticket.domain.Repository.TagRepository
import tregu.helpdesk_ticket.domain.dto.CreateTagResponse
import tregu.helpdesk_ticket.domain.dto.TagResponse
import java.time.OffsetDateTime

@Service
class TagService(private val tagRepository: TagRepository){

    suspend fun createTag(tagName:String): TagResponse {
      return withContext(Dispatchers.IO){ val newTag = TagEntity()
        newTag.name= tagName
        newTag.createdAt = OffsetDateTime.now()
        TagMapper.toResponse(newTag)
        }
    }
}