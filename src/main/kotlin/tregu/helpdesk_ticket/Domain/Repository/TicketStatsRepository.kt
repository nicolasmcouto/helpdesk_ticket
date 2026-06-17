package tregu.helpdesk_ticket.Domain.Repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TicketStatsRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun getBasicStats(): Map<String, Any?> {
        val sql = """
        SELECT 
            COUNT(*) as totalTickets,
            SUM(CASE WHEN status = 'OPEN' THEN 1 ELSE 0 END) as openTickets,
            SUM(CASE WHEN status = 'CLOSED' THEN 1 ELSE 0 END) as closedTickets,
            COALESCE(
                (SELECT AVG(TIMESTAMPDIFF(HOUR, created_at, resolved_at))
                 FROM helpdesk.ticket
                 WHERE resolved_at IS NOT NULL),
                0.0
            ) as averageResolutionTime
        FROM helpdesk.ticket
    """.trimIndent()

        return jdbcTemplate.queryForMap(sql)
    }

    fun getTicketsByStatus(): Map<String, Long> {
        val sql = """
            SELECT 
                status, 
                COUNT(*) as count
            FROM helpdesk.ticket
            GROUP BY status
            ORDER BY count DESC
        """.trimIndent()

        val results = jdbcTemplate.queryForList(sql)
        return results.associate { row ->
            (row["status"] as String) to (row["count"] as Long)
        }
    }

    fun getTicketsByPriority(): Map<String, Long> {
        val sql = """
            SELECT 
                priority, 
                COUNT(*) as count
            FROM helpdesk.ticket
            GROUP BY priority
            ORDER BY count DESC
        """.trimIndent()

        val results = jdbcTemplate.queryForList(sql)
        return results.associate { row ->
            (row["priority"] as String) to (row["count"] as Long)
        }
    }

    fun getTicketsByCategory(): Map<String, Long> {
        val sql = """
            SELECT 
                COALESCE(category, 'Sem categoria') as category, 
                COUNT(*) as count
            FROM helpdesk.ticket
            GROUP BY category
            ORDER BY count DESC
        """.trimIndent()

        val results = jdbcTemplate.queryForList(sql)
        return results.associate { row ->
            (row["category"] as String) to (row["count"] as Long)
        }
    }

    fun getEscalatedStats(): MutableMap<String, Any?> {
        val sql = """
            SELECT 
                COUNT(*) as totalEscalated,
                COUNT(CASE WHEN status != 'CLOSED' THEN 1 END) as openEscalated
            FROM helpdesk.ticket
            WHERE escalated = true
        """.trimIndent()

        return jdbcTemplate.queryForMap(sql)
    }
}