# Event Pass Management System - Complete Backend

Simple interview-ready Spring Boot backend.

## Stack

- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA
- PostgreSQL
- Spring Security
- JJWT 0.12.6
- BCrypt

## Run

Create PostgreSQL database:

```sql
CREATE DATABASE eventpass;
```

Set environment variables if required:

```text
DB_URL=jdbc:postgresql://localhost:5432/eventpass
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_long_random_secret_at_least_32_characters
```

Then:

```bash
mvn clean spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

## JWT authentication flow

```text
POST /api/auth/login
        |
        v
Find user by email
        |
        v
Compare password with BCrypt hash
        |
        v
Generate signed JWT
        |
        +--> user id
        +--> email
        +--> name
        +--> role
        |
        v
Return token to React
        |
        v
React sends:
Authorization: Bearer <token>
        |
        v
JwtAuthenticationFilter verifies token
        |
        v
Spring Security knows user + role
        |
        v
hasRole(...) controls protected APIs
```

Important:
The password is NEVER placed inside the JWT. The password is used only during login to verify the user.

## Demo users

```text
ADMIN:
admin@example.com / admin123

ORGANIZER:
organizer@example.com / organizer123

PARTICIPANT:
participant@example.com / participant123
```

## API list

```text
POST   /api/auth/login

GET    /api/users/{id}

GET    /api/events
GET    /api/events/{eventId}
POST   /api/events
GET    /api/events/my
PUT    /api/events/{eventId}/open
PUT    /api/events/{eventId}/close
DELETE /api/events/{eventId}

GET    /api/events/{eventId}/timeslots
GET    /api/timeslots/{slotId}

POST   /api/registrations
GET    /api/registrations/my
GET    /api/registrations/{registrationId}
PUT    /api/registrations/{registrationId}/cancel
GET    /api/events/{eventId}/registrations

POST   /api/registrations/{registrationId}/pass
GET    /api/passes/{passId}
PUT    /api/passes/{passId}/check-in
PUT    /api/passes/{passId}/check-out
```

## Classes and methods

### User
Entity representing an application user.

Fields:
- id
- name
- email
- password
- phone
- role

Roles:
- ADMIN
- ORGANIZER
- PARTICIPANT

### Event
Entity representing an event.

Important fields:
- title
- description
- eventType
- startTime
- endTime
- venue
- capacity
- availableSeats
- status
- organizer

### TimeSlot
Represents one selectable slot for an event.

Important fields:
- startTime
- endTime
- capacity
- availableSeats
- event

### Registration
Connects a participant, event and time slot.

Important fields:
- user
- event
- timeSlot
- status
- registeredAt

### Pass
Represents the generated event pass.

Important fields:
- passCode
- registration
- status
- checkInAt
- checkOutAt

## Security classes

### JwtService

```text
generateToken(User user)
parseToken(String token)
```

`generateToken()` creates the JWT after login succeeds.

`parseToken()` verifies the token signature and expiration.

### JwtAuthenticationFilter

```text
doFilterInternal(...)
```

Reads:

```text
Authorization: Bearer <token>
```

Then:
1. Parses JWT
2. Gets user id
3. Gets role
4. Creates Spring Security Authentication
5. Places authentication in SecurityContext

### SecurityConfig

```text
securityFilterChain(...)
passwordEncoder()
corsConfigurationSource()
```

`securityFilterChain()` defines authentication and role access.

`passwordEncoder()` provides BCrypt.

`corsConfigurationSource()` allows the React frontend to call the backend.

## Services and important methods

### AuthService

```text
login(LoginRequest request)
```

Finds the user, checks BCrypt password and generates JWT.

### UserService

```text
getById(Long id)
```

Returns a user by id.

### EventService

```text
getAll()
getById(Long id)
create(CreateEventRequest request, Long organizerId)
getMyEvents(Long organizerId)
open(Long id, Long organizerId)
close(Long id, Long organizerId)
delete(Long id, Long organizerId)
```

### TimeSlotService

```text
getByEvent(Long eventId)
getById(Long id)
create(Long eventId, CreateTimeSlotRequest request)
```

### RegistrationService

```text
create(Long userId, CreateRegistrationRequest request)
getMy(Long userId)
getById(Long id)
cancel(Long id, Long userId)
getForEvent(Long eventId, Long organizerId)
```

### PassService

```text
create(Long registrationId, Long userId)
getById(Long passId, Long userId)
checkIn(Long passId, Long userId)
checkOut(Long passId, Long userId)
```

## Role access

```text
ADMIN
  |
  +-- organizer/admin event management

ORGANIZER
  |
  +-- create event
  +-- view own events
  +-- open/close own event
  +-- delete own event
  +-- view registrations for own event

PARTICIPANT
  |
  +-- view events
  +-- register
  +-- cancel own registration
  +-- generate own pass
  +-- view own pass
  +-- check-in
  +-- check-out
```

## Interview explanation

Keep the explanation simple:

"After successful login, I verify the email and BCrypt password. Then I generate a signed JWT containing the user's id and role. For every protected request, the JWT filter validates the token and puts the user's role into Spring Security. Based on that role, SecurityConfig allows or rejects the request. The password itself is never stored in the token."

## Important note about database mapping

The exact ER/database image was not available in the current conversation when this backend was generated. Therefore these entities use the API contract and the Event/Organizer/Participant domain discussed earlier as the working schema. If your approved ER diagram has different column names, IDs, or relationships, change the entity mappings to match that schema before using `ddl-auto=update`.
