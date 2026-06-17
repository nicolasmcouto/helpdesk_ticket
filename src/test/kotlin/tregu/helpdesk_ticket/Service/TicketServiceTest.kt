package tregu.helpdesk_ticket.Service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tregu.helpdesk_ticket.Domain.Enum.TicketPriority
import tregu.helpdesk_ticket.Domain.Repository.TicketRepository
import tregu.helpdesk_ticket.Domain.Repository.TicketStatsRepository
import tregu.helpdesk_ticket.Domain.dto.CreateTicketRequest
import tregu.helpdesk_ticket.Workflow.Dto.ClassificationResult

class TicketServiceTest {

    private val ticketRepository = mockk<TicketRepository>()
    private val ticketStatsRepository = mockk<TicketStatsRepository>()
    private val service = TicketService(ticketRepository, ticketStatsRepository)

    @Test
    fun CreateFunctionSavingWithCorrectata() = runTest {
        every { ticketRepository.save(any()) } answers {firstArg()}

        val classification =  ClassificationResult(TicketPriority.MEDIUM,"category")
        val request = CreateTicketRequest("title","description")
        val result = service.create(request,"Author", classification)

        verify { ticketRepository.save(match{it.title == "title"}) }
    }
}