-- Enable UUID generation 
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- users
CREATE TABLE IF NOT EXISTS users (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email         VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  enabled       BOOLEAN NOT NULL DEFAULT TRUE,
  locked_until  TIMESTAMPTZ NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- clients
CREATE TABLE IF NOT EXISTS clients (
  id                         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id                  VARCHAR(128) NOT NULL UNIQUE,
  client_secret_hash         VARCHAR(255) NOT NULL,
  name                       VARCHAR(255) NOT NULL,
  allowed_grants             TEXT NOT NULL,
  allowed_scopes             TEXT NOT NULL,
  access_token_ttl_seconds   INT NOT NULL DEFAULT 900,
  refresh_token_ttl_seconds  INT NOT NULL DEFAULT 2592000,
  created_at                 TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at                 TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- refresh tokens
CREATE TABLE IF NOT EXISTS refresh_tokens (
  id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  token_hash             VARCHAR(255) NOT NULL UNIQUE,
  user_id                UUID NULL REFERENCES users(id),
  client_id              UUID NOT NULL REFERENCES clients(id),
  issued_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at             TIMESTAMPTZ NOT NULL,
  revoked_at             TIMESTAMPTZ NULL,
  replaced_by_token_id   UUID NULL REFERENCES refresh_tokens(id),
  reuse_detected         BOOLEAN NOT NULL DEFAULT FALSE,
  user_agent             TEXT NULL,
  ip                     TEXT NULL
);

CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_client_id ON refresh_tokens(client_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);