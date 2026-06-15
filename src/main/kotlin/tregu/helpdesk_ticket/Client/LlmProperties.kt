package tregu.helpdesk_ticket.Client

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "llm")
data class LlmProperties(
    val apiKey: String,
    val model: String = "gpt-4.1-mini",
    val dryRun: Boolean = false
)