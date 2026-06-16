package tregu.helpdesk_ticket.Llm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import tregu.helpdesk_ticket.Llm.Dto.ChatCompletionRequest
import tregu.helpdesk_ticket.Llm.Dto.ChatCompletionResponse
import tregu.helpdesk_ticket.Llm.Dto.ChatMessage


@Component
class LlmClient(
    private val openAiWebClient: WebClient,
    private val props: LlmProperties
) {
    suspend fun complete(systemPrompt: String, userPrompt: String): String {
        val request = ChatCompletionRequest(
            model = props.model,
            messages = listOf(
                ChatMessage(role = "system", content = systemPrompt),
                ChatMessage(role = "user", content = userPrompt)
            )
        )

        val response = withContext(Dispatchers.IO) {
            openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .awaitBody<ChatCompletionResponse>()
        }

        return response.choices.first().message.content
    }
}
