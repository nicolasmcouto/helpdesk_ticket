package tregu.helpdesk_ticket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import tregu.helpdesk_ticket.Client.LlmProperties

@EnableConfigurationProperties(LlmProperties::class)
@SpringBootApplication
class HelpdeskTicketApplication

fun main(args: Array<String>) {
	runApplication<HelpdeskTicketApplication>(*args)
}
