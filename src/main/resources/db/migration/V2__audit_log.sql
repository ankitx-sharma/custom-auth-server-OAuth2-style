-- Audit Table
CREATE TABLE IF NOT EXISTS audit_log (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type    VARCHAR(64) NOT NULL,
  user_id       UUID NULL,
  client_id     VARCHAR(128) NULL,
  ip            TEXT NULL,
  user_agent    TEXT NULL,
  metadata      JSONB NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_audit_log_created_at ON audit_log(created_at);
CREATE INDEX IF NOT EXISTS idx_audit_log_event_type ON audit_log(event_type);