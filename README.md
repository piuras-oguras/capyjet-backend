# CapyJet Backend

A Spring Boot REST API for managing student project offers published by universities. Universities publish offers describing a project; the offer can be accepted, at which point the university's contact details are revealed to the accepting party.

## Tech stack

- Java 17
- Spring Boot 4.1.1 (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`)
- PostgreSQL (runtime datasource)
- H2 (in-memory, currently used for local runs — see `application.properties`)
- Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito + AssertJ (tests)

## Getting started

```bash
./mvnw spring-boot:run
```

The app starts on the default port `8080`. With the current configuration it uses an in-memory H2 database (recreated on every restart, `ddl-auto=create-drop`), so no external database setup is required to run it locally.

- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:capyjest`, user `sa`, empty password)
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

To run the tests:

```bash
./mvnw test
```

## Domain model

### University
Represents an institution that can publish offers.

| Field | Type | Notes |
|---|---|---|
| `id` | Long | generated |
| `name` | String | required, unique |
| `contactEmail` | String | |
| `contactPhone` | String | |

### Offer
Represents a project proposal published by a university.

| Field | Type | Notes |
|---|---|---|
| `id` | Long | generated |
| `title` | String | required |
| `description` | String | required, max 5000 chars |
| `status` | `OfferStatus` | `OPEN` (default), `ACCEPTED`, `CLOSED` |
| `university` | University | required, many-to-one |
| `createdAt` | LocalDateTime | set automatically on persist |

An offer can only transition from `OPEN` to `ACCEPTED` (see [Business rules](#business-rules)).

## API endpoints

### Offers — `/api/offers`

| Method | Path | Description |
|---|---|---|
| GET | `/api/offers` | List all offers, optionally filtered by `?status=OPEN\|ACCEPTED\|CLOSED`, ordered by creation date (newest first) |
| GET | `/api/offers/{id}` | Get a single offer by ID |
| POST | `/api/offers` | Create a new offer (`OfferRequest` body) |
| PUT | `/api/offers/{id}` | Update an existing offer's title, description and university |
| DELETE | `/api/offers/{id}` | Delete an offer |
| POST | `/api/offers/{id}/accept` | Accept an open offer and reveal the university's contact details |

**`OfferRequest` (create/update body)**

```json
{
  "title": "string, required, max 200 chars",
  "description": "string, required, max 5000 chars",
  "universityId": "number, required"
}
```

**`OfferResponse`**

```json
{
  "id": 1,
  "title": "Traffic prediction",
  "description": "...",
  "status": "OPEN",
  "universityName": "Bydgoszcz University of Technology",
  "createdAt": "2026-09-17T12:00:00"
}
```

**`AcceptedOfferResponse`** (returned by `POST /api/offers/{id}/accept`)

```json
{
  "offerId": 1,
  "status": "ACCEPTED",
  "contact": {
    "universityName": "Bydgoszcz University of Technology",
    "email": "contact@pbs.edu.pl",
    "phone": "123456789"
  }
}
```

### Universities — `/api/universities`

| Method | Path | Description |
|---|---|---|
| GET | `/api/universities` | List all universities |
| GET | `/api/universities/{id}` | Get a single university by ID |
| POST | `/api/universities` | Create a new university (`CreateUniversityRequest` body) |

**`CreateUniversityRequest`**

```json
{
  "name": "string, required, max 200 chars",
  "contactEmail": "string, required, valid email",
  "contactPhone": "string, max 20 chars"
}
```

**`UniversityResponse`** — only exposes `id` and `name`; contact details are only revealed through the offer-acceptance flow.

## Business rules

- An offer can be accepted (`POST /api/offers/{id}/accept`) only while its status is `OPEN`. Attempting to accept an offer that is already `ACCEPTED` or `CLOSED` results in a `409 Conflict`.
- University contact details (`email`, `phone`) are never exposed through the general `University`/`Offer` read endpoints — they are only returned as part of `AcceptedOfferResponse` once an offer has been accepted.

## Error handling

All errors are returned as a JSON `ErrorResponse`:

```json
{
  "status": 404,
  "message": "Resource Offer with ID 1 not found",
  "errors": {},
  "timestamp": "2026-09-17T12:00:00"
}
```

| Exception | HTTP status | Trigger |
|---|---|---|
| `NotFoundException` | 404 Not Found | Offer or University not found by ID |
| `OfferNotAvailableException` | 409 Conflict | Attempting to accept an offer that is not `OPEN` |
| `MethodArgumentNotValidException` | 400 Bad Request | Request body fails bean validation (`errors` map contains per-field messages) |

## Project structure

```
src/main/java/pl/capyjet/backend/
├── CapyjetBackendApplication.java
├── common/exception/          # Global exception handling
├── offer/                     # Offer entity, controller, service, repository, DTOs
└── university/                # University entity, controller, service, repository, DTOs
```
