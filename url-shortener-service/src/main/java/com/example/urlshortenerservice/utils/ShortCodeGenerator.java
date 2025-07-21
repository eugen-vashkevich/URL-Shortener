package com.example.urlshortenerservice.utils;

/**
 * Utility class for generating short codes using Base62 encoding. Base62 encoding is used to
 * convert a long integer ID into a shorter, alphanumeric string, suitable for use as a short URL
 * code. This ensures that each generated short code is unique, assuming the input ID is unique
 * (e.g., a primary key from a database).
 */
public class ShortCodeGenerator {
  private static final String ALPHABET =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int BASE = ALPHABET.length();

  /**
   * Encodes a given long integer into a Base62 string. This method converts a numerical ID
   * (typically a database primary key) into a compact, human-readable, and URL-friendly short code.
   *
   * <p>The encoding process works by repeatedly dividing the number by the base (62) and using the
   * remainder to select a character from the {@code ALPHABET}. The characters are appended in
   * reverse order of calculation to form the final code.
   *
   * @param num The long integer number to be encoded. Must be non-negative.
   * @return A {@link String} representing the Base62 encoded short code. Returns "0" if the input
   *     number is 0.
   */
  public static String encodeToBase62(long num) {
    if (num == 0) {
      return String.valueOf(ALPHABET.charAt(0));
    }

    final var result = new StringBuilder();

    while (num > 0) {
      result.insert(0, ALPHABET.charAt((int) (num % BASE)));
      num /= BASE;
    }

    return result.toString();
  }
}
