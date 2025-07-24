package com.example.urlshortenerreadservice.repository;

import com.example.urlshortenerreadservice.models.URL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

/** Managing URL data in the database */
@Repository
public class URLRepositoryImpl implements URLRepository {

  private final JdbcTemplate jdbcTemplate;

  public URLRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Retrieves a {@link URL} object from the database based on its short URL code. This method
   * queries the 'urls' table and maps the result to a {@link URL} object.
   *
   * @param shortCode The short URL code to search for.
   * @return An {@link Optional} containing the {@link URL} object if found, or an empty {@link
   *     Optional} if no URL matches the given short code.
   */
  @Override
  public Optional<URL> findByShortCode(String shortCode) {
    final var sql =
        "SELECT id, original_url, short_url_code, created_at, expires_at FROM urls WHERE short_url_code = ?";
    final var result = jdbcTemplate.query(sql, rowMapper, shortCode);
    return result.stream().findFirst();
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
