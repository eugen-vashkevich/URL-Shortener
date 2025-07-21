package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.models.URL;

import java.util.Optional;

public interface URLRepository {
    Optional<URL> save(URL url);
}
