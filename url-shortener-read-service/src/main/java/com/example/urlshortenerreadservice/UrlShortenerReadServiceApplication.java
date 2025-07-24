package com.example.urlshortenerreadservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerReadServiceApplication {

  public static void main(String[] args) {
    final var dotenv =
        Dotenv.configure()
            .directory(System.getProperty("user.home"))
            .filename(".env")
            .systemProperties()
            .load();

    SpringApplication.run(UrlShortenerReadServiceApplication.class, args);
  }
}
