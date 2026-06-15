CREATE TABLE IF NOT EXISTS ticket (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  title              TEXT NOT NULL,
  description        TEXT NOT NULL,
  status             ENUM('OPEN','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN',
  priority           ENUM('LOW','MEDIUM','HIGH','URGENT') NOT NULL DEFAULT 'MEDIUM',
  category           VARCHAR(255),
  created_by         VARCHAR(255) NOT NULL,
  assigned_to        VARCHAR(255),
  escalated          BOOLEAN NOT NULL DEFAULT false,
  escalated_at       DATETIME(6),
  escalation_summary TEXT,
  created_at         DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at         DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  resolved_at        DATETIME(6)
);

CREATE INDEX ix_ticket_status ON ticket (status);
CREATE INDEX ix_ticket_escalated ON ticket (escalated);

CREATE TABLE IF NOT EXISTS comment (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  ticket_id   BIGINT NOT NULL,
  author      VARCHAR(255) NOT NULL,
  content     TEXT NOT NULL,
  created_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT fk_comment_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE
);

CREATE INDEX ix_comment_ticket ON comment (ticket_id);

CREATE TABLE IF NOT EXISTS tag (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) NOT NULL UNIQUE,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS ticket_tag (
  ticket_id  BIGINT NOT NULL,
  tag_id     BIGINT NOT NULL,
  PRIMARY KEY (ticket_id, tag_id),
  CONSTRAINT fk_tt_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
  CONSTRAINT fk_tt_tag    FOREIGN KEY (tag_id)    REFERENCES tag(id)    ON DELETE CASCADE
);
