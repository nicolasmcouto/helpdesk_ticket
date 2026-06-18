package tregu.helpdesk_ticket.Service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tools.jackson.databind.ObjectMapper
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import tregu.helpdesk_ticket.Llm.LlmClient
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LlmServiceTest {
    private val llmClient = mockk<LlmClient>()
    private val objectMapper = ObjectMapper()
    private val service = LlmService(llmClient, objectMapper)

    @Test
    fun classifyReturnsCorrectPriorityAndCategory() = runTest {
        coEvery { llmClient.complete(any(), any()) } returns """{"priority":"HIGH","category":"login"}"""

        val result = service.classify("Login quebrado", "Não consigo acessar")

        assertEquals(TicketPriority.HIGH, result.priority)
        assertEquals("login", result.category)
    }

    @Test
    fun classifyParsesAllValidPriorities() = runTest {
        for (priority in listOf("LOW", "MEDIUM", "HIGH")) {
            coEvery { llmClient.complete(any(), any()) } returns """{"priority":"$priority","category":"other"}"""

            val result = service.classify("titulo", "descricao")

            assertEquals(TicketPriority.valueOf(priority), result.priority)
        }
    }

    @Test
    fun classifyThrowsOnInvalidJson() = runTest {
        coEvery { llmClient.complete(any(), any()) } returns "json invalido"

        assertFailsWith<Exception> { service.classify("titulo", "descricao") }
    }

    @Test
    fun summarizeReturnsExactLlmResponse() = runTest {
        val expected = "Ticket sem resposta há 25 horas. Problema crítico relatado."
        coEvery { llmClient.complete(any(), any()) } returns expected

        val result = service.summarize("titulo", "descricao", TicketStatus.OPEN, OffsetDateTime.now())

        assertEquals(expected, result)
    }

    @Test
    fun summarizeIncludesTitleAndDescriptionInPrompt() = runTest {
        coEvery { llmClient.complete(any(), any()) } returns "resumo"

        service.summarize("Falha crítica no login", "Usuários sem acesso", TicketStatus.IN_PROGRESS, OffsetDateTime.now())

        coVerify {
            llmClient.complete(any(), match { prompt ->
                prompt.contains("Falha crítica no login") && prompt.contains("Usuários sem acesso")
            })
        }
    }
}
