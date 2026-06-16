package tregu.helpdesk_ticket.domain.ticket.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import tregu.helpdesk_ticket.domain.Entity.TicketEntity
import java.time.OffsetDateTime

@Entity
@Table(name = "comment")
class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    var ticketId: TicketEntity? = null

    @Column(nullable = false)
    var author: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = ""

    @CreationTimestamp
    var createdAt: OffsetDateTime? = null
}