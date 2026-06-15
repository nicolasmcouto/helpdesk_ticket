package tregu.helpdesk_ticket.domain.Entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import tregu.helpdesk_ticket.domain.Enum.TicketPriority
import tregu.helpdesk_ticket.domain.Enum.TicketStatus
import tregu.helpdesk_ticket.domain.ticket.entity.CommentEntity
import java.time.OffsetDateTime
import java.time.OffsetTime


@Entity
@Table(name = "ticket")
class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var title: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = ""

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TicketStatus = TicketStatus.OPEN

    var category: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: TicketPriority = TicketPriority.MEDIUM

    @Column(nullable = false)
    var createdBy: String = ""

    var assignedTo: String? = null

    @Column(nullable = false)
    var escalated: Boolean = false

    var escalatedAt: OffsetDateTime? = null

    @Column(columnDefinition = "TEXT")
    var escalationSummary: String? = null

    @CreationTimestamp
    var createdAt: OffsetDateTime? = null

    @UpdateTimestamp
    var updatedAt: OffsetDateTime? = null

    var resolvedAt: OffsetDateTime? = null

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var comments: MutableList<CommentEntity> = mutableListOf()

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ticket_tag",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<TagEntity> = mutableSetOf()
}

