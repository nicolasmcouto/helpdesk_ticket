package tregu.helpdesk_ticket.Workflow

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.Domain.dto.CreateTicketResponse
import tregu.helpdesk_ticket.Service.LlmService
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult
import java.time.OffsetDateTime
import kotlin.test.assertEquals

class CreateTicketWorkflowTest {
    private val llmService = mockk<LlmService>()
    private val ticketService = mockk<TicketService>()
    private val workflow = CreateTicketWorkflow(llmService, ticketService)

    private val now = OffsetDateTime.now()
    private val classification = ClassificationResult(TicketPriority.HIGH, "login")
    private val response = CreateTicketResponse(
        id = 1L, title = "Login quebrado", description = "Não consigo acessar",
        status = TicketStatus.OPEN, priority = TicketPriority.HIGH,
        category = "login", createBy = "user", assignedTo = null,
        escalated = false, createdAt = now, updatedAt = now
    )

    @Test
    fun executeClassifiesThenCreatesTicket() = runTest {
        val request = CreateTicketRequest("Login quebrado", "Não consigo acessar")
        coEvery { llmService.classify(any(), any()) } returns classification
        coEvery { ticketService.create(any(), any(), any()) } returns response

        val result = workflow.execute(request, "user")

        coVerify { llmService.classify("Login quebrado", "Não consigo acessar") }
        coVerify { ticketService.create(request, "user", classification) }
        assertEquals(1L, result.id)
    }

    @Test
    fun executePassesClassificationToTicketService() = runTest {
        val request = CreateTicketRequest("titulo", "descricao")
        coEvery { llmService.classify(any(), any()) } returns classification
        coEvery { ticketService.create(any(), any(), any()) } returns response

        workflow.execute(request, "author")

        coVerify {
            ticketService.create(
                any(), any(),
                match { it.priority == TicketPriority.HIGH && it.category == "login" }
            )
        }
    }
}
