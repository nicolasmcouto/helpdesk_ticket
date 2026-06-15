package tregu.helpdesk_ticket.Client


import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import tregu.helpdesk_ticket.Service.TicketService
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.dto.createTicketRequest
import tregu.helpdesk_ticket.domain.dto.createTicketResponse

data class ClassificationResult(
    val priority: TicketPriority,
    val category: String
)

@Component
class CreateTicketWorkflow(
    private val llmClient: LlmClient,
    private val ticketService: TicketService,
    private val objectMapper: ObjectMapper
) {


    private suspend fun classify(title: String, description: String): ClassificationResult {
        val json = llmClient.complete(SYSTEM_PROMPT, "title: $title\ndescription: $description")
        return objectMapper.readValue(json, ClassificationResult::class.java)

    }

     suspend fun execute(request: createTicketRequest, author: String): createTicketResponse {
        val classification = classify(request.title, request.description)
        return ticketService.create(request, author, classification)
    }

    companion object {
        private val SYSTEM_PROMPT = """
            Você é um classificador de tickets de suporte. Dado o título e a descrição
            de um ticket, classifique a prioridade e a categoria.

            Retorne EXCLUSIVAMENTE JSON válido sem texto extra:
            {"priority": "LOW|MEDIUM|HIGH|URGENT", "category": "<texto curto em inglês>"}

            Critérios de prioridade:
            - URGENT: produção fora, perda de dados, segurança comprometida
            - HIGH: feature crítica quebrada, múltiplos usuários afetados
            - MEDIUM: bug com workaround, request comum
            - LOW: cosmético, dúvida, sugestão

            Categoria: 1-2 palavras lowercase em inglês: billing, login, performance,
            data, api, ui, notifications, security, other.
        """.trimIndent()
    }
}