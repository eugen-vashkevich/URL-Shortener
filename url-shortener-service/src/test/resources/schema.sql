CREATE TABLE IF NOT EXISTS urls
(
    id             BIGSERIAL PRIMARY KEY,
    original_url   TEXT NOT NULL,
    short_url_code VARCHAR(10) UNIQUE,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at     TIMESTAMP WITH TIME ZONE
);
