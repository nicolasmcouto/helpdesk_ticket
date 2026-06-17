package tregu.helpdesk_ticket.Controller


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


import tregu.helpdesk_ticket.Service.CommentService
import tregu.helpdesk_ticket.Domain.dto.CommentResponse
import tregu.helpdesk_ticket.Domain.dto.CreateCommentRequest

@RestController
@RequestMapping("ticket/comment")
class CommentController(private val commentService: CommentService) {

    @PostMapping("/{ticketId}")
    suspend fun createComment(@PathVariable ticketId :Long ,@RequestHeader("X-Author") author: String, @RequestBody content: String): ResponseEntity<CommentResponse>{

        val comment = commentService.create(CreateCommentRequest(ticketId, author, content))
        return ResponseEntity.status(HttpStatus.CREATED).body(comment)
    }

    @GetMapping("/{ticketId}")
    suspend fun findCommentByTicket(@PathVariable ticketId: Long): ResponseEntity<List<CommentResponse>>{

        val list = commentService.findComment(ticketId)
        return ResponseEntity.ok(list)
    }
}