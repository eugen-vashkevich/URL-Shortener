package com.example.urlshortenerreadservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.redis.host=test",
        "spring.data.redis.port=6379",
        "spring.data.redis.password=test"
})
class UrlShortenerReadServiceApplicationTests {

  @Test
  void contextLoads() {}
}
