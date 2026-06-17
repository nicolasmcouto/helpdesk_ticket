package tregu.helpdesk_ticket.Domain.dto

import jakarta.validation.constraints.NotBlank

data class CreateTicketRequest(
    @NotBlank
    val title: String,
    @NotBlank
    val description: String
)
