package tregu.helpdesk_ticket.Service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Entity.TagEntity
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Repository.TagRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import java.time.OffsetDateTime
import java.util.*

class TagServiceTest {
    private val tagRepository = mockk<TagRepository>()
    private val ticketRepository = mockk<TicketRepository>()
    private val service = TagService(tagRepository, ticketRepository)

    @Test
    fun createTag() = runTest {
        val savedTag = TagEntity().apply { id = 1L; name = "bug"; createdAt = OffsetDateTime.now() }
        every { tagRepository.save(any()) } returns savedTag

        val result = service.createNewTag("bug")

        verify { tagRepository.save(match { it.name == "bug" }) }
        assertEquals("bug", result.name)
    }
    @Test
    fun insertTag() = runTest {
        val ticket = TicketEntity().apply { id = 1L; createdAt = OffsetDateTime.now(); updatedAt = OffsetDateTime.now() }
        val tag = TagEntity().apply { id = 10L; name = "bug"; tickets = mutableSetOf(); createdAt = OffsetDateTime.now() }

        every { tagRepository.findByIdWithTickets(10L) } returns tag
        every { ticketRepository.findById(1L) } returns Optional.of(ticket)
        every { tagRepository.save(any()) } returns tag

        service.insertTag(id = 10L, ticketId = 1L)

        assertTrue(tag.tickets.contains(ticket))
        verify { tagRepository.save(tag) }
    }
    @Test
    fun insertTagDoesNotDuplicateExistingAssociation() = runTest {
        val ticket = TicketEntity().apply { id = 1L; createdAt = OffsetDateTime.now(); updatedAt = OffsetDateTime.now() }
        val tag = TagEntity().apply { id = 10L; name = "bug"; tickets = mutableSetOf(ticket); createdAt = OffsetDateTime.now() }

        every { tagRepository.findByIdWithTickets(10L) } returns tag
        every { ticketRepository.findById(1L) } returns Optional.of(ticket)
        every { tagRepository.save(any()) } returns tag

        service.insertTag(id = 10L, ticketId = 1L)

        assertEquals(1, tag.tickets.size)
    }

    @Test
    fun removeTag() = runTest {
        val ticket = TicketEntity().apply { id = 1L }
        val tag = TagEntity().apply { id = 10L; name = "bug"; tickets = mutableSetOf(ticket) }
        every { tagRepository.findByIdWithTickets(10L) } returns tag
        every { ticketRepository.getReferenceById(1L) } returns ticket
        every { tagRepository.save(any()) } returns tag

        service.removeTag(id = 10L, ticketId = 1L)

        assertFalse(tag.tickets.contains(ticket))
        verify { tagRepository.save(tag) }
    }
}