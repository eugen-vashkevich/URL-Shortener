package com.example.urlshortenerservice.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/** Utility class for checking the validity and existence of URLs. */
public class URLChecker {

  /**
   * Checks if a given URL string is valid and accessible. This method attempts to establish a
   * connection to the URL using a HEAD request to determine its existence without downloading the
   * full content. If the URL string does not specify a scheme (e.g., "google.com"), it first
   * attempts to use "https://". If that fails, it then tries "http://".
   *
   * @param urlString The string representation of the URL to check.
   * @return {@code true} if the URL is valid, accessible, and returns an HTTP status code
   *     indicating success (2xx) or redirection (3xx) for either HTTPS or HTTP; {@code false}
   *     otherwise.
   */
  public static boolean isValidURL(String urlString) {
    URL url;

    try {
      final var uri = new URI(urlString);

      if (!uri.isAbsolute()) {
        return false;
      } else {
        url = uri.toURL();
      }

    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }

    final int responseCode = getResponseCode(url);
    return responseCode >= 200 && responseCode < 400;
  }

  /**
   * Attempts to get the HTTP response code for a given URL using a HEAD request.
   *
   * @param url The URL object to connect to.
   * @return The HTTP response code (e.g., 200, 404, 500) if successful, or -1 if an error occurs
   *     during the connection or request process.
   */
  private static int getResponseCode(URL url) {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("HEAD");
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);

      return connection.getResponseCode();
    } catch (IOException e) {
      return -1;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
}
