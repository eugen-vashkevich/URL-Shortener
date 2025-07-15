package com.example.urlshortenerservice.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/** Utility class for checking the validity and existence of URLs. */
public class URLChecker {

  /**
   * Checks if a given URL string is valid and accessible.
   *
   * @param urlString The string representation of the URL to check.
   * @return {@code true} if the URL is valid, accessible, and returns an HTTP status code
   *     indicating success (2xx) or redirection (3xx); {@code false} otherwise.
   * @throws RuntimeException if the URL string is malformed or has a URI syntax error, if an I/O
   *     error occurs during connection setup or response reading, or if the HTTP protocol is not
   *     supported for the HEAD method.
   */
  public static boolean isValidURL(String urlString) {
    final URL url;

    try {
      url = new URI(urlString).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(
          "Error: Malformed URL - " + urlString + ". Details: " + e.getMessage(), e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(
          "Error: URI Syntax Exception - " + urlString + ". Details: " + e.getMessage(), e);
    }

    final int responseCode = getResponseCode(url);

    return responseCode >= 200 && responseCode < 400;
  }

  private static int getResponseCode(URL url) {
    HttpURLConnection URLConnection;

    try {
      URLConnection = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
      throw new RuntimeException(
          "Error: Failed to open connection to URL - " + url + ". Details: " + e.getMessage(), e);
    }

    try {
      URLConnection.setRequestMethod("HEAD");
    } catch (ProtocolException e) {
      throw new RuntimeException(
          "Error: Protocol does not support HEAD method for URL - "
              + url
              + ". Details: "
              + e.getMessage(),
          e);
    }

    URLConnection.setConnectTimeout(2000);
    URLConnection.setReadTimeout(2000);

    final int responseCode;
    try {
      responseCode = URLConnection.getResponseCode();
    } catch (IOException e) {
      throw new RuntimeException(
          "Error: Failed to get response code from URL - " + url + ". Details: " + e.getMessage(),
          e);
    }
    return responseCode;
  }
}
