package com.example.urlshortenerreadservice.service;

import com.example.urlshortenerreadservice.exceprions.UrlNotFoundException;
import com.example.urlshortenerreadservice.models.URL;
import com.example.urlshortenerreadservice.repository.URLRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/** Service class responsible for handling read operations for URL entities. */
@Service
public class URLService {
  private static final byte TIME_OUT_FOR_CACHE = 5;
  private URLRepositoryImpl urlRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public URLService(RedisTemplate<String, Object> redisTemplate, URLRepositoryImpl urlRepository) {
    this.redisTemplate = redisTemplate;
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
    URL cachedUrl = (URL) redisTemplate.opsForValue().get(shortCode);
    if (cachedUrl != null) {
      if (cachedUrl.getExpiresAt() == null
          || cachedUrl.getExpiresAt().isAfter(OffsetDateTime.now())) {
        return Optional.of(cachedUrl);
      } else {
        redisTemplate.delete(shortCode);
      }
    }

    final var url =
        urlRepository
            .findByShortCode(shortCode)
            .orElseThrow(
                () -> new UrlNotFoundException("Short URL '" + shortCode + "' not found."));

    if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(OffsetDateTime.now())) {
      throw new UrlNotFoundException("Short URL '" + shortCode + "' has expired.");
    }

    redisTemplate.opsForValue().set(shortCode, url, 5, TimeUnit.HOURS);

    return Optional.of(url);
  }
}
