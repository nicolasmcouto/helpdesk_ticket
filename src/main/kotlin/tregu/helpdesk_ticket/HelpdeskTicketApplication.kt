package tregu.helpdesk_ticket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import tregu.helpdesk_ticket.Llm.LlmProperties

@EnableScheduling
@EnableConfigurationProperties(LlmProperties::class)
@SpringBootApplication
class HelpdeskTicketApplication

fun main(args: Array<String>) {
	runApplication<HelpdeskTicketApplication>(*args)
}
