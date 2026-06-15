package tregu.helpdesk_ticket.domain.Entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name="tag")
class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?= null

    @Column(nullable = false, unique = true)
    var name:String = ""

    @CreationTimestamp
    var createdAt: OffsetDateTime?= null

    @ManyToMany(mappedBy = "tags")
    var tickets: MutableSet<TicketEntity> = mutableSetOf()
}