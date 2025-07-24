package com.example.urlshortenerreadservice.service;

import com.example.urlshortenerreadservice.exceprions.UrlNotFoundException;
import com.example.urlshortenerreadservice.models.URL;
import com.example.urlshortenerreadservice.repository.URLRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

/** Service class responsible for handling read operations for URL entities. */
@Service
public class URLService {
  private URLRepositoryImpl urlRepository;

  public URLService(URLRepositoryImpl urlRepository) {
    this.urlRepository = urlRepository;
  }

  /**
   * Retrieves the original URL and its associated details based on a given short code.
   *
   * @param shortCode The unique short code of the URL to retrieve.
   * @return An {@link Optional} containing the {@link URL} object if found and valid.
   * @throws UrlNotFoundException If no URL is found for the given short code, or if the found URL
   *     has expired.
   */
  @Transactional(readOnly = true)
  public Optional<URL> getOriginalUrl(String shortCode) {
    final var url =
        urlRepository
            .findByShortCode(shortCode)
            .orElseThrow(
                () -> new UrlNotFoundException("Short URL '" + shortCode + "' not found."));

    if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(OffsetDateTime.now())) {
      throw new UrlNotFoundException("Short URL '" + shortCode + "' has expired.");
    }

    return Optional.of(url);
  }
}
