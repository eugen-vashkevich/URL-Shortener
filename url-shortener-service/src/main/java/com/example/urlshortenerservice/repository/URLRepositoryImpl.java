package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.models.URL;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class URLRepositoryImpl implements URLRepository {

  private final JdbcTemplate jdbcTemplate;

  public URLRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<URL> save(URL url) throws DuplicateKeyException {
    final var sql =
        "INSERT INTO urls (original_url, short_url_code, created_at, expires_at) VALUES (?, ?, ?, ?)";
    final var affectedRows =
        jdbcTemplate.update(
            sql,
            url.getOriginalUrl(),
            url.getShortUrlCode(),
            url.getCreatedAt(),
            url.getExpiresAt());

    return affectedRows > 0 ? Optional.of(url) : Optional.empty();
  }

  private final RowMapper<URL> rowMapper =
      new RowMapper<URL>() {
        @Override
        public URL mapRow(ResultSet rs, int rowNum) throws SQLException {
          final var urlMapping = new URL();
          urlMapping.setId(rs.getLong("id"));
          urlMapping.setOriginalUrl(rs.getString("original_url"));
          urlMapping.setShortUrlCode(rs.getString("short_url"));
          urlMapping.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
          urlMapping.setExpiresAt(rs.getObject("expires_at", OffsetDateTime.class));

          return urlMapping;
        }
      };
}
