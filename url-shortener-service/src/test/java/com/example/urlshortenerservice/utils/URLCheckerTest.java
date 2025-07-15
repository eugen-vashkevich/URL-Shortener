package com.example.urlshortenerservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class URLCheckerTest {
  @Test
  void URLIsValid_WhenCheckingURL_ThenReturnTrue() {
    // Given
    final var urlToCheck = "https://google.com/";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertTrue(resultOfChecking);
  }

  @Test
  void URLIsValid_WhenCheckingAccessibleURLWithPath_ThenReturnTrue() {
    // Given
    final var urlToCheck = "https://www.wikipedia.org/wiki/Main_Page";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertTrue(resultOfChecking);
  }

  @Test
  void URLIsInvalid_WhenCheckingMalformedURL_ThenReturnFalse() {
    // Given
    final var urlToCheck = "not-valid-url.com";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertFalse(resultOfChecking);
  }

  @Test
  void URLWithoutScheme_WhenCheckingCorrectURL_ThenReturnTrue() {
    // Given
    final var urlToCheck = "google.com";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertTrue(resultOfChecking);
  }

  @Test
  void URLIsInvalid_WhenCheckingNonExistentPath_ThenReturnFalse() {
    // Given
    final var urlToCheck = "https://www.google.com/this/path/definitely/does/not/exist/12345";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertFalse(resultOfChecking);
  }

  @Test
  void URLIsInvalid_WhenCheckingEmptyURL_ThenReturnFalse() {
    // Given
    final var urlToCheck = "";

    // When
    final var resultOfChecking = URLChecker.isValidURL(urlToCheck);

    // Then
    Assertions.assertFalse(resultOfChecking, "Checking an empty URL string should return false.");
  }
}
