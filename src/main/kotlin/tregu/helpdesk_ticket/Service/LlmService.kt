package tregu.helpdesk_ticket.Service

import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import tregu.helpdesk_ticket.Llm.LlmClient
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult
import tregu.helpdesk_ticket.Domain.Enum.TicketStatus
import java.time.OffsetDateTime

@Service
class LlmService(
    private val llmClient: LlmClient,
    private val objectMapper: ObjectMapper) {

     suspend fun classify(title: String, description: String): ClassificationResult {
        val json = llmClient.complete(CLASSIFY_SYSTEM_PROMPT, "title: $title\ndescription: $description")
        return objectMapper.readValue(json, ClassificationResult::class.java)

    }

    suspend fun summarize(title: String, description: String, status: TicketStatus, createdAt: OffsetDateTime): String{
        return  llmClient.complete(SUMMARIZE_SYSTEM_PROMPT,
            "title: {$title}\n" +
                "description: {$description}\n" +
                "Status: {$status}\n" +
                "CreatedAt: {$createdAt}\n" +
                "timeNow: {${OffsetDateTime.now()}}")

    }


    companion object {
        private val CLASSIFY_SYSTEM_PROMPT = """
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

        private val SUMMARIZE_SYSTEM_PROMPT ="""
            Você é um assistente especializado em suporte técnico. Sua tarefa é gerar um resumo de escalonamento claro e objetivo para tickets que ficaram sem resposta por mais de 24 horas.
            O resumo deve:
            - Explicar brevemente o problema relatado
            - Indicar o tempo sem interação
            - Sugerir o nível de urgência percebido
            - Ter no máximo 3 frases
            - Retorne a quantas horas está em espera de atendimento
            - Ser escrito em português
        """.trimIndent()
    }
}