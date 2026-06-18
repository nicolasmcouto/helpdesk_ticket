package tregu.helpdesk_ticket.Workflow

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Entity.TicketEntity
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.dto.TicketDetail
import tregu.helpdesk_ticket.Service.LlmService
import tregu.helpdesk_ticket.Service.TicketService
import java.time.OffsetDateTime
import kotlin.test.assertEquals

class EscalateWorkflowTest {
    private val llmService = mockk<LlmService>()
    private val ticketService = mockk<TicketService>()
    private val workflow = EscalateWorkflow(llmService, ticketService)

    private val now = OffsetDateTime.now()

    private fun buildTicketEntity() = TicketEntity().apply {
        id = 1L
        title = "Login quebrado"
        description = "Sem acesso desde ontem"
        status = TicketStatus.OPEN
        priority = TicketPriority.HIGH
        createdAt = now
        updatedAt = now
    }

    private fun buildTicketDetail(summary: String) = TicketDetail(
        id = 1L, title = "Login quebrado", description = "Sem acesso desde ontem",
        status = TicketStatus.OPEN, priority = TicketPriority.HIGH,
        category = null, createdBy = "", assignedTo = null,
        escalated = true, escalatedAt = now, escalationSummary = summary,
        tags = emptyList(), comments = emptyList(),
        createdAt = now, updatedAt = now, resolvedAt = null
    )

    @Test
    fun executeFindsSummarizesAndEscalates() = runTest {
        val entity = buildTicketEntity()
        val summary = "Ticket sem resposta há 25 horas."
        val detail = buildTicketDetail(summary)

        coEvery { ticketService.findTicket(1L) } returns entity
        coEvery { llmService.summarize(any(), any(), any(), any()) } returns summary
        coEvery { ticketService.escalate(any(), any()) } returns detail

        val result = workflow.execute(1L)

        coVerify { ticketService.findTicket(1L) }
        coVerify { llmService.summarize("Login quebrado", "Sem acesso desde ontem", TicketStatus.OPEN, now) }
        coVerify { ticketService.escalate(entity, summary) }
        assertEquals(summary, result.escalationSummary)
    }

    @Test
    fun executeReturnsTicketDetailFromEscalate() = runTest {
        val entity = buildTicketEntity()
        val detail = buildTicketDetail("resumo gerado")

        coEvery { ticketService.findTicket(1L) } returns entity
        coEvery { llmService.summarize(any(), any(), any(), any()) } returns "resumo gerado"
        coEvery { ticketService.escalate(any(), any()) } returns detail

        val result = workflow.execute(1L)

        assertEquals(detail, result)
    }
}
