CREATE TABLE IF NOT EXISTS urls
(
    id           BIGSERIAL PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_url    VARCHAR(10),
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at   TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_short_url ON urls (short_url);

CREATE INDEX IF NOT EXISTS idx_urls_expires_at ON urls (expires_at);
