package com.example.urlshortenerservice.models;

import java.time.OffsetDateTime;

public class ErrorResponse {
  private int status;
  private String error;
  private String message;
  private OffsetDateTime timestamp;
  private String path;

  public ErrorResponse(int status, String error, String message, String path) {
    this.status = status;
    this.error = error;
    this.message = message;
    this.timestamp = OffsetDateTime.now();
    this.path = path;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public String getPath() {
    return path;
  }
}
