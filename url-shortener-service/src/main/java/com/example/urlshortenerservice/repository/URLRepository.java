package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.models.URL;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface URLRepository {
    Optional<URL> save(URL url);
    Optional<URL> updateShortUrlCode(long id, String shortUrlCode);
    Optional<URL> findById(long id);
    int deleteExpiresUrl(OffsetDateTime currentTime);
}
