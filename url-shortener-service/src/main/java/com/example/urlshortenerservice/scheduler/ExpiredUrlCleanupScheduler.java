package com.example.urlshortenerservice.scheduler;

import com.example.urlshortenerservice.service.URLService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;

/** Scheduled task component responsible for periodically cleaning up expired URLs. */
@Component
public class ExpiredUrlCleanupScheduler {
  private final URLService urlService;

  public ExpiredUrlCleanupScheduler(URLService urlService) {
    this.urlService = urlService;
  }

  /**
   * This method is scheduled to run periodically to delete expired URLs from the database.
   */
  @Scheduled(fixedRate = 86400000)
  public void cleanupExpiredUrls() {
    int deletedCount = urlService.deleteExpiredUrls(OffsetDateTime.now());
    System.out.println("Deleted " + deletedCount + " expired URLs.");
  }
}
