package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.models.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.OffsetDateTime;

@JdbcTest
@Import({URLRepositoryImpl.class})
class URLRepositoryImplTest {
  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private URLRepositoryImpl urlRepository;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("DELETE FROM urls");
  }

  @Test
  void save_WhenSavingNewURL_ThenURLIsSavedAndReturned() {
    // Given
    final var originalUrl = "https://example.com/long/path/to/resource";
    final var shortUrlCode = "short123";
    final var createdAt = OffsetDateTime.now();
    final var expiresAt = createdAt.plusDays(7);

    final var urlToSave = new URL();
    urlToSave.setOriginalUrl(originalUrl);
    urlToSave.setShortUrlCode(shortUrlCode);
    urlToSave.setCreatedAt(createdAt);
    urlToSave.setExpiresAt(expiresAt);

    // When
    final var savedUrlOptional = urlRepository.save(urlToSave);

    final var savedUrl = savedUrlOptional.get();

    // Then
    Assertions.assertTrue(savedUrlOptional.isPresent());
    Assertions.assertNotNull(savedUrl.getId());
    Assertions.assertEquals(originalUrl, savedUrl.getOriginalUrl());
    Assertions.assertEquals(shortUrlCode, savedUrl.getShortUrlCode());
    Assertions.assertEquals(createdAt.toEpochSecond(), savedUrl.getCreatedAt().toEpochSecond());
    Assertions.assertEquals(expiresAt.toEpochSecond(), savedUrl.getExpiresAt().toEpochSecond());
  }

  @Test
  void save_WhenSavingTwoEqualsURLS_ThenURLIsNotSaved() {
    // Given
    final var firstOriginalUrl = "https://example.com/long/path/to/resource";
    final var firstShortUrlCode = "short123";
    final var firstURLCreatedAt = OffsetDateTime.now();
    final var firstURLExpiresAt = firstURLCreatedAt.plusDays(7);

    final var firstURLToSave = new URL();
    firstURLToSave.setOriginalUrl(firstOriginalUrl);
    firstURLToSave.setShortUrlCode(firstShortUrlCode);
    firstURLToSave.setCreatedAt(firstURLCreatedAt);
    firstURLToSave.setExpiresAt(firstURLExpiresAt);

    final var secondOriginalUrl = "https://example.com/long/path/to/resource";
    final var secondShortUrlCode = "short123";
    final var secondURLCreatedAt = OffsetDateTime.now();
    final var secondURLExpiresAt = firstURLCreatedAt.plusDays(7);

    final var secondURLToSave = new URL();
    secondURLToSave.setOriginalUrl(secondOriginalUrl);
    secondURLToSave.setShortUrlCode(secondShortUrlCode);
    secondURLToSave.setCreatedAt(secondURLCreatedAt);
    secondURLToSave.setExpiresAt(secondURLExpiresAt);

    // When
    urlRepository.save(firstURLToSave);

    // Then
    Assertions.assertThrows(DuplicateKeyException.class, () -> urlRepository.save(secondURLToSave));
  }

  @Test
  void updateShortUrlCode_WhenIdExists_ThenShortUrlCodeIsUpdated() {
    // Given
    final var initialUrl = new URL();
    initialUrl.setOriginalUrl("https://update.me");
    initialUrl.setCreatedAt(OffsetDateTime.now());
    initialUrl.setExpiresAt(OffsetDateTime.now().plusDays(1));
    initialUrl.setShortUrlCode(null);

    final var savedUrl = urlRepository.save(initialUrl).get();
    final var newShortCode = "updated789";

    // When
    final var updatedUrlOptional = urlRepository.updateShortUrlCode(savedUrl.getId(), newShortCode);

    // Then
    Assertions.assertTrue(updatedUrlOptional.isPresent());
    final var updatedUrl = updatedUrlOptional.get();
    Assertions.assertEquals(savedUrl.getId(), updatedUrl.getId());
    Assertions.assertEquals(newShortCode, updatedUrl.getShortUrlCode());
  }

  @Test
  void updateShortUrlCode_WhenIdDoesNotExist_ThenThrowDataRetrievalFailureException() {
    // Given
    final long nonExistentId = 999L;
    final var shortCode = "nonexistent";

    // When and Then
    Assertions.assertThrows(
        DataRetrievalFailureException.class,
        () -> urlRepository.updateShortUrlCode(nonExistentId, shortCode),
        "Updating a non-existent ID should throw DataRetrievalFailureException");
  }

  @Test
  void findById_WhenURLFound_ThenReturnOptionalOfURL() {
    // Given
    final var urlToSave = new URL();
    urlToSave.setOriginalUrl("https://find.me/by/id");
    urlToSave.setShortUrlCode("findbyid");
    urlToSave.setCreatedAt(OffsetDateTime.now());
    urlToSave.setExpiresAt(OffsetDateTime.now().plusDays(1));
    final var savedUrl = urlRepository.save(urlToSave).get();

    // When
    final var foundUrlOptional = urlRepository.findById(savedUrl.getId());

    // Then
    Assertions.assertTrue(foundUrlOptional.isPresent(), "URL should be found by ID");
    final var foundUrl = foundUrlOptional.get();
    Assertions.assertEquals(savedUrl.getId(), foundUrl.getId());
    Assertions.assertEquals(savedUrl.getOriginalUrl(), foundUrl.getOriginalUrl());
    Assertions.assertEquals(savedUrl.getShortUrlCode(), foundUrl.getShortUrlCode());
  }
}
