# URL Shortner

Spring Boot 4 REST service for generating short, redirectable URLs backed by MySQL. The service exposes a minimal API to create new short links and redirect visitors while tracking click counts.

## Features
- Create 8-character alphanumeric short codes for any http/https URL
- 302 redirects from short code to the original URL
- Click counter increments on every redirect
- Input validation on URL format
- Hello endpoint for a lightweight liveness check

## Tech Stack
- Java 25
- Spring Boot 4.0.2 (Web MVC, Data JPA, Validation)
- MySQL (runtime), JPA/Hibernate for persistence
- Maven wrapper for builds
- Lombok for boilerplate reduction

## Getting Started
1) Install Java 25 and Maven (wrapper included).
2) Set the datasource connection string:
   - `CLOUD_MYSQL_URI` (required) e.g. `jdbc:mysql://localhost:3306/urlshortner?user=root&password=secret`.
3) Run the service:
```bash
./mvnw spring-boot:run
```
4) Service listens on `http://localhost:8080` by default.

### Configuration
Key properties in `src/main/resources/application.properties`:
- `spring.application.name` = `URLShortner`
- `spring.datasource.url` = `${CLOUD_MYSQL_URI}`
- `spring.jpa.hibernate.ddl-auto` = `update` (auto-creates/updates the `urls` table)
- `spring.jpa.show-sql` = `true` (logs SQL; disable in production)

## API Reference
Base path: `http://localhost:8080/v1/api`

### Create a short URL
- **POST** `/v1/api`
- Body:
```json
{
  "url": "https://example.com/articles/123"
}
```
- Responses:
  - `201 Created` with payload
  ```json
  {
    "originalUrl": "https://example.com/articles/123",
    "shortUrl": "http://localhost:8080/v1/api/Ab3Cd9Ef",
    "createdDate": "2025-01-01T00:00:00",
    "clickCount": 0
  }
  ```
  - `400 Bad Request` if the URL is blank or does not start with `http://` or `https://`.

### Redirect to original URL
- **GET** `/v1/api/{shortCode}`
- Behavior: returns `302 Found` with `Location` header set to the original URL and increments `clickCount`.
- Errors: `404` if the short code is unknown.

### Health check
- **GET** `/hello`
- Returns `Hello World!` for quick sanity testing.

## Data Model
Table: `urls`
- `id` (PK, auto)
- `original_url`
- `short_url` (unique)
- `created_date`
- `click_count`

## Building and Testing
- Run tests:
```bash
./mvnw test
```
- Build executable jar:
```bash
./mvnw clean package
```
Artifact will be at `target/URLShortner-0.0.1-SNAPSHOT.jar`.

## Deployment Notes
- Set `CLOUD_MYSQL_URI` in the target environment; include credentials and SSL parameters as needed.
- Behind a reverse proxy, set `server.forward-headers-strategy` and externalize the host/port so generated `shortUrl` values reflect the public base URL.
- Disable `spring.jpa.show-sql` and configure production logging/metrics as required.

## Troubleshooting
- `java.lang.RuntimeException: Short URL not found` → verify the short code exists in the database.
- Connection errors on startup → confirm `CLOUD_MYSQL_URI` is reachable and credentials are correct.
