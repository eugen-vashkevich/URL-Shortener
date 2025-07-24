package com.example.urlshortenerreadservice.repository;

import com.example.urlshortenerreadservice.models.URL;

import java.util.Optional;

public interface URLRepository {
    Optional<URL> findByShortCode(String shortCode);
}
