package com.example.urlshortenerreadservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/** Configuration class for setting up and customizing Redis integration with Spring Data Redis. */
@Configuration
public class RedisConfig {

  /**
   * Configures and provides a custom {@link RedisTemplate} bean.
   *
   * <p>This method sets up the connection factory and defines custom serializers to handle complex
   * objects, preventing common serialization errors. The key serializer is set to {@link
   * StringRedisSerializer} for human-readable keys, while the value serializer uses {@link
   * GenericJackson2JsonRedisSerializer} to convert Java objects to JSON format.
   *
   * @param connectionFactory The Redis connection factory provided by Spring Boot's
   *     auto-configuration.
   * @return A fully configured {@link RedisTemplate} instance.
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    final var template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(connectionFactory);

    final var jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper());

    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    template.setValueSerializer(jsonSerializer);
    template.setHashValueSerializer(jsonSerializer);

    template.afterPropertiesSet();

    return template;
  }

  /**
   * Provides a custom configured {@link ObjectMapper} bean.
   *
   * <p>This custom mapper is essential for correct serialization of complex Java objects,
   * particularly those containing Java 8 date/time types like {@link java.time.OffsetDateTime}. It
   * registers the {@link JavaTimeModule} to support these types and activates default typing to
   * handle polymorphic types during deserialization, ensuring that the original object type is
   * preserved.
   *
   * @return A configured {@link ObjectMapper} instance.
   */
  private ObjectMapper objectMapper() {
    final var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.activateDefaultTyping(
        mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
    return mapper;
  }
}
