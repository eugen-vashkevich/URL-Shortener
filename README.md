# URL Shortener Microservices

A high-performance URL shortener system built with Spring Boot, designed with a distributed microservices architecture.
This project features separate services for read and write operations, database replication for scalability and
reliability, and Redis for caching frequently accessed URLs.

## Features ✨

1. **Scalable Microservices Architecture**: Two distinct services to handle write and read operations independently.
    - **write-service**: Handles the creation of new shortened URLs.
    - **read-service**: Manages the redirection from short URLs to the original URLs.
1. **Database Replication**: A master-slave database setup ensures high availability and load distribution.
    - The write-service connects to the master database.
    - The read-service connects to the slave (replica) database.
1. **Caching with Redis**: Frequently accessed URLs are cached in Redis to provide near-instantaneous redirection and
   reduce database load.
1. **Containerized**: The entire application is containerized using Docker for easy setup and deployment.

## Architecture Overview 🏛️

The system is designed to be highly scalable and resilient by separating the write and read concerns.

<img width="1112" height="552" alt="image" src="https://github.com/user-attachments/assets/1d276822-74b7-4baf-b622-3436febc8b4e" />

1. **Write Operations**: When a user wants to shorten a URL, the request is sent to the write-service. This service
   generates a unique short code, saves the mapping to the master database, and returns the shortened URL to the user.
1. **Read Operations**: When a user accesses a shortened URL, the request hits the read-service.
    - The service first checks Redis for the original URL.
    - If the URL is not in the cache, it queries the slave database.
    - Once retrieved, the original URL is cached in Redis for future requests, and the user is redirected.

## Getting Started 🚀

You can read the
instructions [here](https://github.com/eugen-vashkevich/URL-Shortener/blob/main/CONTRIBUTING.md#how-to-run-project-locally)
how to start the project.

## API Endpoints 📖

### Write Service

#### Shorten a URL

**Endpoint**: POST /api/v1/shorten

Request Body:

```json
{
  "longUrl": "https://your-very-long-url.com/with/a/lot/of/path/segments"
}
```

Success Response (201 Created):

```json
{
  "id": 1,
  "originalUrl": "https://your-very-long-url.com/with/a/lot/of/path/segments",
  "shortUrlCode": "1",
  "createdAt": "2025-07-31T12:59:46.941683Z",
  "expiresAt": "2025-08-07T12:59:46.941683Z"
}
```

### Read Service

#### Redirect to Original URL

**Endpoint**: GET /{shortCode}

**Example**: GET /aBcDeF1

Success Response (302 Found):

Redirects the user to the corresponding long URL.

## License 📄

This project is licensed under the MIT License. See
the [LICENSE](https://github.com/eugen-vashkevich/URL-Shortener/blob/30-add-readme-file/LICENSE) file for details.
