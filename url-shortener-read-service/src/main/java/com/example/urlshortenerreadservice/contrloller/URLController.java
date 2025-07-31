package com.example.urlshortenerreadservice.contrloller;

import com.example.urlshortenerreadservice.exceptions.UrlNotFoundException;
import com.example.urlshortenerreadservice.service.URLService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

/** REST Controller for handling URL redirection and retrieval of URL details. */
@RestController
public class URLController {

  private final URLService urlService;

  public URLController(URLService urlService) {
    this.urlService = urlService;
  }

  /**
   * Handles redirection for a given short URL code. If the short URL is found and not expired, it
   * redirects to the original URL. Otherwise, it returns a 404 Not Found error.
   *
   * @param shortCode The short code to redirect from.
   * @param response The HttpServletResponse to perform the redirection.
   */
  @GetMapping("/{shortCode}")
  public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response)
      throws IOException {
    try {
      final var url = urlService.getOriginalUrl(shortCode);

      if (url.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }

      response.sendRedirect(url.get().getOriginalUrl());
    } catch (UrlNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
