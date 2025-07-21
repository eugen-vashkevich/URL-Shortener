package com.example.urlshortenerservice.controller;

import com.example.urlshortenerservice.models.ErrorResponse;
import com.example.urlshortenerservice.models.URL;
import com.example.urlshortenerservice.service.URLService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling URL shortening operations. This controller exposes endpoints for
 * shortening original URLs and handles common exceptions, returning appropriate HTTP status codes
 * and error messages.
 */
@RestController
public class URLController {

  private final URLService urlShorteningService;

  public URLController(URLService urlShorteningService) {
    this.urlShorteningService = urlShorteningService;
  }

  /**
   * Handles the POST request to shorten a given URL.
   *
   * <p>This endpoint expects an original URL in the request body. It delegates the shortening
   * process to the {@link URLService} and returns the shortened URL entity along with an HTTP 201
   * Created status.
   *
   * @param request A {@link URL} object containing the original URL to be shortened.
   * @return A {@link ResponseEntity} containing the created {@link URL} entity (with generated
   *     short code and ID) and an {@link HttpStatus#CREATED} (201).
   */
  @PostMapping("/api/v1/urls")
  public ResponseEntity<URL> shortenUrl(@RequestBody URL request) {
    final URL urlToShorten = new URL();
    urlToShorten.setOriginalUrl(request.getOriginalUrl());

    final var shortenedUrlEntity = urlShorteningService.shortenURL(urlToShorten);

    return new ResponseEntity<>(shortenedUrlEntity.get(), HttpStatus.CREATED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    StringBuilder errors = new StringBuilder();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String errorMessage = error.getDefaultMessage();
              errors.append(errorMessage).append("; ");
            });

    return new ResponseEntity<>(
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed: " + errors.toString().trim(),
            request.getRequestURI()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {
    return new ResponseEntity<>(
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()),
        HttpStatus.BAD_REQUEST);
  }
}
