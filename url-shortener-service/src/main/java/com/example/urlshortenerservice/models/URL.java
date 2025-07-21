package com.example.urlshortenerservice.models;

import java.time.OffsetDateTime;

public class URL {
    private long id;
    private String originalUrl;
    private String shortUrlCode;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;

    public URL() {
    }

    public URL(long id, String originalUrl, String shortUrlCode, OffsetDateTime createdAt, OffsetDateTime expiresAt) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrlCode = shortUrlCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrlCode() {
        return shortUrlCode;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setShortUrlCode(String shortUrlCode) {
        this.shortUrlCode = shortUrlCode;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
