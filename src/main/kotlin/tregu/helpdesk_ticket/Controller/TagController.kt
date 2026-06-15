package tregu.helpdesk_ticket.Controller

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tregu.helpdesk_ticket.Service.TagService
import tregu.helpdesk_ticket.domain.dto.CreateTagResponse
import tregu.helpdesk_ticket.domain.dto.TagResponse

@RestController
@RequestMapping("ticket/tag")
class TagController(private val service: TagService) {

    @PostMapping
    suspend fun createTag(@RequestBody tagName: String): ResponseEntity<TagResponse> {
        val tag = service.createTag(tagName)

    }


}