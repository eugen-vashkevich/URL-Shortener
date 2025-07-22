package com.example.urlshortenerservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlShortenerServiceApplication {

  public static void main(String[] args) {
    final var dotenv =
        Dotenv.configure()
            .directory(System.getProperty("user.home"))
            .filename(".env")
            .systemProperties()
            .load();

    SpringApplication.run(UrlShortenerServiceApplication.class, args);
  }
}
