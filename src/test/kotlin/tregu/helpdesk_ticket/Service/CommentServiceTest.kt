package tregu.helpdesk_ticket.Service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.Repository.CommentRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.dto.CreateCommentRequest
import tregu.helpdesk_ticket.Domain.ticket.entity.CommentEntity
import java.time.OffsetDateTime

class CommentServiceTest {
    private val commentRepository = mockk<CommentRepository>()
    private val ticketRepository = mockk<TicketRepository>()
    private val service = CommentService(commentRepository, ticketRepository)

    private fun buildTicketEntity(
        id: Long = 1L,
        priority: TicketPriority = TicketPriority.LOW,
        status: TicketStatus = TicketStatus.OPEN,
        now: OffsetDateTime = OffsetDateTime.now()
    ) = TicketEntity().apply {
        this.id = id
        this.priority = priority
        this.status = status
        this.createdAt = now
        this.updatedAt = now
    }

    @Test
    fun createCommentAndAssignetedToTicket() = runTest {
        var ticket = buildTicketEntity()
        var comment = CommentEntity().apply {
            id = 10L
            author = "author"
            content = "content"
            ticket = ticket
            createdAt = OffsetDateTime.now()
        }

        every { ticketRepository.getReferenceById(1L) } returns ticket
        every { commentRepository.save(any()) } answers {
            firstArg<CommentEntity>().also {
                it.id = 10L
                it.createdAt = OffsetDateTime.now()
            }
        }

        val result = service.create(CreateCommentRequest(ticketId = 1L, author = "author", content = "content"))

        verify { ticketRepository.getReferenceById(1L) }
        verify { commentRepository.save(match{it.author == "author" && it.content == "content"}) }
        assertEquals("content", result.content)
        assertEquals("author", result.author)
    }

    @Test
    fun findCommentReturningMapedList() = runTest {
        val ticketRef = TicketEntity().apply { id = 1L }
        val comments = listOf(
            CommentEntity().apply { id = 1L; author = "a"; content = "c1"; ticket = ticketRef; createdAt = OffsetDateTime.now() },
            CommentEntity().apply { id = 2L; author = "b"; content = "c2"; ticket = ticketRef; createdAt = OffsetDateTime.now() }
        )
        every { commentRepository.findByTicketId(1L) } returns comments

        val result = service.findComment(1L)

        assertEquals(2, result.size)
        verify { commentRepository.findByTicketId(1L) }
    }

    @Test
    fun findCommentReturnEmptyList() = runTest {
        every { commentRepository.findByTicketId(1L) } returns emptyList()

        val result = service.findComment(1L)

        assertTrue(result.isEmpty())
        verify { commentRepository.findByTicketId(1L) }
    }
}
