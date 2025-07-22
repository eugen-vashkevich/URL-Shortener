package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.models.URL;
import com.example.urlshortenerservice.repository.URLRepositoryImpl;
import com.example.urlshortenerservice.utils.ShortCodeGenerator;
import com.example.urlshortenerservice.utils.URLChecker;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Service class responsible for handling the core business logic of URL shortening. This includes
 * validating URLs, generating unique short codes, persisting URL data, and retrieving original URLs
 * from short codes. It orchestrates operations between the controller and the repository layers.
 */
@Service
public class URLService {

  private final URLRepositoryImpl urlRepository;
  private final int URL_LIFE_LENGTH = 7;

  public URLService(URLRepositoryImpl urlRepository) {
    this.urlRepository = urlRepository;
  }

  /**
   * Shortens a given original URL.
   *
   * <p>This operation is transactional, ensuring atomicity (either all steps succeed or all are
   * rolled back).
   *
   * @param urlToBeShorten A {@link URL} object containing at least the original URL.
   * @return The {@link URL} entity with the generated short code and database ID.
   * @throws IllegalArgumentException If the original URL is invalid, inaccessible, or if the
   *     initial save fails unexpectedly.
   * @throws DuplicateKeyException If a unique constraint is violated during the save operation
   *     (e.g., trying to save the exact same URL again if a unique constraint is on original_url).
   */
  @Transactional
  public Optional<URL> shortenURL(URL urlToBeShorten) {
    if (!URLChecker.isValidURL(urlToBeShorten.getOriginalUrl())) {
      throw new IllegalArgumentException(
          "Original URL is not valid or accessible: " + urlToBeShorten.getOriginalUrl());
    }

    final var urlToSaveWithoutId = new URL();
    urlToSaveWithoutId.setOriginalUrl(urlToBeShorten.getOriginalUrl());
    urlToSaveWithoutId.setCreatedAt(OffsetDateTime.now());
    urlToSaveWithoutId.setExpiresAt(urlToSaveWithoutId.getCreatedAt().plusDays(URL_LIFE_LENGTH));

    final Optional<URL> urlToSaveWithID;
    try {
      urlToSaveWithID = urlRepository.save(urlToSaveWithoutId);
    } catch (DuplicateKeyException e) {
      throw new DuplicateKeyException("Error with saving this URL");
    }

    if (urlToSaveWithID.isEmpty()) {
      throw new IllegalArgumentException("Failed to save URL");
    }

    final var url = urlToSaveWithID.get();

    final var shortCode = ShortCodeGenerator.encodeToBase62(url.getId());
    url.setShortUrlCode(shortCode);

    return urlRepository.updateShortUrlCode(url.getId(), shortCode);
  }

  /**
   * Deletes URLs that have expired before the given current time.
   *
   * @param currentTime The current time to compare against the expiration date.
   * @return The number of URLs deleted.
   */
  @Transactional
  public int deleteExpiredUrls(OffsetDateTime currentTime) {
    return urlRepository.deleteExpiresUrl(currentTime);
  }
}
