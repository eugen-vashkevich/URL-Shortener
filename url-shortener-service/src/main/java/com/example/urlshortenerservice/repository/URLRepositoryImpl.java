package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.models.URL;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Repository implementation for managing {@link URL} entities in the database. This class uses
 * Spring's {@link JdbcTemplate} to interacts directly with the 'urls' table.
 */
@Repository
public class URLRepositoryImpl implements URLRepository {

  private final JdbcTemplate jdbcTemplate;

  public URLRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Saves a new {@link URL} entity to the database. This method performs an INSERT operation and
   * attempts to retrieve the auto-generated ID for the new record.
   *
   * @param url The {@link URL} object to be saved. The 'id' field should typically be null before
   *     saving, as it will be populated by the database.
   * @return An {@link Optional} containing the saved {@link URL} object with its generated ID, or
   *     an empty Optional if the save operation affected no rows.
   * @throws DuplicateKeyException If a unique constraint violation occurs (e.g., trying to insert
   *     an original URL that already exists with a unique constraint).
   * @throws DataRetrievalFailureException If the insert operation fails to return a generated key
   *     despite affecting rows.
   */
  @Override
  public Optional<URL> save(URL url) throws DuplicateKeyException {
    final var sql =
        "INSERT INTO urls (original_url, short_url_code, created_at, expires_at) VALUES (?, ?, ?, ?)";

    final var keyHolder = new GeneratedKeyHolder();

    int affectedRows =
        jdbcTemplate.update(
            connection -> {
              final var ps = connection.prepareStatement(sql, new String[] {"id"});
              ps.setString(1, url.getOriginalUrl());
              ps.setString(2, url.getShortUrlCode());
              ps.setObject(3, url.getCreatedAt());
              ps.setObject(4, url.getExpiresAt());
              return ps;
            },
            keyHolder);

    if (affectedRows > 0) {
      if (keyHolder.getKey() != null) {
        url.setId(keyHolder.getKey().longValue());
      } else {
        System.err.println(
            "Warning: Insert successful but generated ID was not retrieved for URL: "
                + url.getOriginalUrl());
      }
      return Optional.of(url);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Updates the {@code short_url_code} for an existing {@link URL} record identified by its ID.
   *
   * @param id The ID of the URL record to update.
   * @param shortUrlCode The new short code to set.
   * @return An {@link Optional} containing the updated {@link URL} object if found and updated, or
   *     an empty Optional if the record with the given ID was not found after update.
   * @throws DataRetrievalFailureException If no record was found or updated for the given ID, or if
   *     the updated record could not be retrieved.
   */
  @Override
  public Optional<URL> updateShortUrlCode(long id, String shortUrlCode) {
    final var sql = "UPDATE urls SET short_url_code = ? WHERE id = ?";
    final var affectedRows = jdbcTemplate.update(sql, shortUrlCode, id);
    if (affectedRows == 0) {
      throw new DataRetrievalFailureException(
          "Failed to update click count for short URL: "
              + shortUrlCode
              + ". Record not found or not updated.");
    }

    return Optional.ofNullable(
        findById(id)
            .orElseThrow(
                () ->
                    new DataRetrievalFailureException(
                        "Failed to retrieve updated URL for Id: " + id)));
  }

  /**
   * Finds a {@link URL} record by its database ID.
   *
   * @param id The ID of the URL record to find.
   * @return An {@link Optional} containing the {@link URL} object if found, or an empty Optional if
   *     no record matches the given ID.
   */
  @Override
  public Optional<URL> findById(long id) {
    final var sql =
        "SELECT id, original_url, short_url_code, created_at, expires_at FROM urls WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  private final RowMapper<URL> rowMapper =
      new RowMapper<URL>() {
        @Override
        public URL mapRow(ResultSet rs, int rowNum) throws SQLException {
          final var urlMapping = new URL();
          urlMapping.setId(rs.getLong("id"));
          urlMapping.setOriginalUrl(rs.getString("original_url"));
          urlMapping.setShortUrlCode(rs.getString("short_url_code"));
          urlMapping.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
          urlMapping.setExpiresAt(rs.getObject("expires_at", OffsetDateTime.class));

          return urlMapping;
        }
      };
}
