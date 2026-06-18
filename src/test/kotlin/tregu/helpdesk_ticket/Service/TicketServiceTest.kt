package tregu.helpdesk_ticket.Service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketStatsRepository
import tregu.helpdesk_ticket.Domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.Domain.dto.UpdateTicketRequest
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TicketServiceTest {

    private val ticketRepository = mockk<TicketRepository>()
    private val ticketStatsRepository = mockk<TicketStatsRepository>()
    private val service = TicketService(ticketRepository, ticketStatsRepository)

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
    fun CreateFunctionSavingWithCorrectInfo() = runTest {
        every { ticketRepository.save(any()) } answers {
            firstArg<TicketEntity>().also {
                it.id = 1L
                it.createdAt = OffsetDateTime.now()
                it.updatedAt = OffsetDateTime.now()
            }
        }
        val classification = ClassificationResult(TicketPriority.MEDIUM, "category")
        val request = CreateTicketRequest("title", "description")
        service.create(request, "Author", classification)

        verify { ticketRepository.save(match { it.title == "title" }) }
    }

    @Test
    fun GetTicketDetailWithReturnNotNull() = runTest {
        val ticketEntity = buildTicketEntity(id = 1L).apply {
            title = "title"
            description = "description"
            priority = TicketPriority.MEDIUM
        }
        every { ticketRepository.findById(1L) } returns Optional.of(ticketEntity)

        val result = service.getTicketDetail(1L)

        assertNotNull(result)
        assertEquals("title", result?.title)
    }

    @Test
    fun GetTicketDetailWithReturnNull() = runTest {
        every { ticketRepository.findById(1L) } returns Optional.empty()

        val result = service.getTicketDetail(1L)

        assertNull(result)
    }

    @Test
    fun findWithFilterAllNullRequest() = runTest {
        every { ticketRepository.findWithFilters(null, null, null, null) } returns emptyList()

        val result = service.findWithFilter(null, null, null, null)

        assertTrue(result.isEmpty())
    }

    @Test
    fun findWithFilterReturningCorrectEntities() = runTest {
        val entities = listOf(
            buildTicketEntity(id = 1L, priority = TicketPriority.HIGH).apply { title = "Ticket 1" },
            buildTicketEntity(id = 2L, priority = TicketPriority.LOW).apply { title = "Ticket 2" }
        )
        every { ticketRepository.findWithFilters(null, null, null, null) } returns entities

        val result = service.findWithFilter(null, null, null, null)

        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Ticket 1", result[0].title)
    }

    @Test
    fun updateWithOnlyField() = runTest {
        val entity = Optional.of(buildTicketEntity(id = 1L, priority = TicketPriority.LOW))

        every { ticketRepository.findById(1L) } returns entity
        every { ticketRepository.save(any()) } returns entity.get()

        service.ticketUpdate(1L, UpdateTicketRequest(status = TicketStatus.IN_PROGRESS))

        verify {
            ticketRepository.save(match {
                it.status == TicketStatus.IN_PROGRESS &&
                        it.priority == TicketPriority.LOW &&
                        it.assignedTo == null
            })
        }
    }
    @Test
    fun updateAllFilds()= runTest{
        val entity = Optional.of(buildTicketEntity(id = 1L, priority = TicketPriority.LOW))

        every { ticketRepository.findById(1L) } returns entity
        every { ticketRepository.save(any()) } returns entity.get()

        service.ticketUpdate(1L, UpdateTicketRequest(status = TicketStatus.IN_PROGRESS, assignedTo = "Nicolas", priority = TicketPriority.HIGH))

        verify {
            ticketRepository.save(match {
                it.status == TicketStatus.IN_PROGRESS &&
                        it.priority == TicketPriority.HIGH &&
                        it.assignedTo == "Nicolas"
            })
        }
    }
    @Test
    fun getStatsConvert0ToNull() = runTest {
        every { ticketStatsRepository.getBasicStats() } returns mapOf(
            "totalTickets" to null,
            "openTickets" to 2,
            "closedTickets" to BigDecimal(3)
        )
        every { ticketStatsRepository.getEscalatedStats() } returns mutableMapOf()
        every { ticketStatsRepository.getTicketsByStatus() } returns emptyMap()
        every { ticketStatsRepository.getTicketsByPriority() } returns emptyMap()
        every { ticketStatsRepository.getTicketsByCategory() } returns emptyMap()

        val result = service.getStats()

        assertEquals(0, result.totalTickets)
    }

    @Test
    fun escalateSelectEscalatedAndReturnDetail() = runTest {
        val entity = buildTicketEntity()
        every { ticketRepository.save(any()) }returns entity

        var result = service.escalate(entity,"summary")

        assertEquals("summary", result.escalationSummary)
        assertTrue(result.escalated)
        assertNotNull(result.escalatedAt)
        assertEquals(1L, result.id)
    }

    @Test
    fun findTicketThrowsWhenNotFound() = runTest {
        every { ticketRepository.findById(99L) } returns Optional.empty()

        assertFailsWith<NullPointerException> { service.findTicket(99L) }
    }

    @Test
    fun findTicketsForEscalationDelegatesToRepository() = runTest {
        val entities = listOf(buildTicketEntity(id = 1L), buildTicketEntity(id = 2L))
        every { ticketRepository.findTicketsWithoutRecentComments() } returns entities

        val result = service.findTicketsForEscalation()

        assertEquals(2, result.size)
        verify { ticketRepository.findTicketsWithoutRecentComments() }
    }

    @Test
    fun ticketUpdateReturnsNullWhenNotFound() = runTest {
        every { ticketRepository.findById(99L) } returns Optional.empty()

        val result = service.ticketUpdate(99L, UpdateTicketRequest(status = TicketStatus.RESOLVED))

        assertNull(result)
    }

    @Test
    fun createAppliesClassificationFieldsToEntity() = runTest {
        every { ticketRepository.save(any()) } answers {
            firstArg<TicketEntity>().also {
                it.id = 1L
                it.createdAt = OffsetDateTime.now()
                it.updatedAt = OffsetDateTime.now()
            }
        }
        val classification = ClassificationResult(TicketPriority.HIGH, "security")
        val request = CreateTicketRequest("Falha de segurança", "Sistema comprometido")

        service.create(request, "admin", classification)

        verify {
            ticketRepository.save(match {
                it.priority == TicketPriority.HIGH && it.category == "security"
            })
        }
    }

}