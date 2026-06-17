package tregu.helpdesk_ticket.Controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tregu.helpdesk_ticket.Service.TagService
import tregu.helpdesk_ticket.Domain.dto.CreateTagResponse
import tregu.helpdesk_ticket.Domain.dto.InsertTagResponse

@RestController
@RequestMapping("ticket")
class TagController(private val tagService: TagService) {

    @PostMapping("/tag")
    suspend fun createTag(@RequestBody name:String): ResponseEntity<CreateTagResponse>{

        val tag = tagService.createNewTag(name)
        return ResponseEntity.status(HttpStatus.CREATED).body(tag)
    }

    @PatchMapping("/{ticketId}/tag/{id}")
    suspend fun insertTag(@PathVariable ticketId: Long, @PathVariable id: Long): ResponseEntity<InsertTagResponse> {
        val tag = tagService.insertTag(id, ticketId)
        return ResponseEntity.status(HttpStatus.OK).body(tag)
    }

    @DeleteMapping("/{ticketId}/tag/{id}")
    suspend fun removeTag(@PathVariable ticketId: Long, @PathVariable id: Long): ResponseEntity<Unit>{
        tagService.removeTag(id, ticketId)
        return ResponseEntity.noContent().build()
    }
}