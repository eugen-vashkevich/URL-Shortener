package com.example.urlshortenerreadservice.exceprions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UrlNotFoundException() {
    super();
  }

  public UrlNotFoundException(String message) {
    super(message);
  }

  public UrlNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public UrlNotFoundException(Throwable cause) {
    super(cause);
  }

  protected UrlNotFoundException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
