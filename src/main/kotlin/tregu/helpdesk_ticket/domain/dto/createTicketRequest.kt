package tregu.helpdesk_ticket.domain.dto

import jakarta.validation.constraints.NotBlank

data class createTicketRequest(
    @NotBlank
    val title: String,
    @NotBlank
    val description: String
)
