package tregu.helpdesk_ticket.Job

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.dto.TicketDetail
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.Workflow.EscalateWorkflow
import java.time.OffsetDateTime

class EscalationJobTest {
    private val ticketService = mockk<TicketService>()
    private val escalateWorkflow = mockk<EscalateWorkflow>()
    private val job = EscalationJob(ticketService, escalateWorkflow)

    private val now = OffsetDateTime.now()

    private fun buildTicketEntity(id: Long) = TicketEntity().apply {
        this.id = id
        this.status = TicketStatus.OPEN
        this.priority = TicketPriority.MEDIUM
        this.createdAt = now
        this.updatedAt = now
    }

    private fun buildTicketDetail(id: Long) = TicketDetail(
        id = id, title = "", description = "",
        status = TicketStatus.OPEN, priority = TicketPriority.MEDIUM,
        category = null, createdBy = "", assignedTo = null,
        escalated = true, escalatedAt = now, escalationSummary = "resumo",
        tags = emptyList(), comments = emptyList(),
        createdAt = now, updatedAt = now, resolvedAt = null
    )

    @Test
    fun escalatesAllTicketsFound() {
        coEvery { ticketService.findTicketsForEscalation() } returns listOf(
            buildTicketEntity(1L),
            buildTicketEntity(2L)
        )
        coEvery { escalateWorkflow.execute(1L) } returns buildTicketDetail(1L)
        coEvery { escalateWorkflow.execute(2L) } returns buildTicketDetail(2L)

        job.escalateTickets()

        coVerify(exactly = 1) { escalateWorkflow.execute(1L) }
        coVerify(exactly = 1) { escalateWorkflow.execute(2L) }
    }

    @Test
    fun doesNotCallWorkflowWhenNoTickets() {
        coEvery { ticketService.findTicketsForEscalation() } returns emptyList()

        job.escalateTickets()

        coVerify(exactly = 0) { escalateWorkflow.execute(any()) }
    }
}
