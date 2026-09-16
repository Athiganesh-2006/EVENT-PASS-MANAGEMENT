# Event Pass Management System - Frontend

Simple React + Vite frontend for the interview Event Pass Management System.

## Stack

- React
- Vite
- Axios
- React Router

## Run

```bash
npm install
npm run dev
```

Frontend:
http://localhost:5173

Backend expected by default:
http://localhost:8080

If the backend uses another URL, copy `.env.example` to `.env` and change:

```text
VITE_API_URL=http://localhost:8080/api
```

## Main pages

- `/login`
- `/events`
- `/events/:eventId`
- `/create-event`
- `/my-events`
- `/registrations`
- `/registrations/:registrationId`
- `/passes/:passId`

## API calls implemented

### Authentication

```text
POST /api/auth/login
```

### User

```text
GET /api/users/{id}
```

### Events

```text
GET    /api/events
GET    /api/events/{eventId}
POST   /api/events
GET    /api/events/my?organizerId={id}
PUT    /api/events/{eventId}/open
PUT    /api/events/{eventId}/close
DELETE /api/events/{eventId}
```

### Time slots

```text
GET /api/events/{eventId}/timeslots
GET /api/timeslots/{slotId}
```

### Registrations

```text
POST /api/registrations
GET /api/registrations/my?userId={id}
GET /api/registrations/{registrationId}
PUT /api/registrations/{registrationId}/cancel
GET /api/events/{eventId}/registrations
```

### Passes

```text
POST /api/registrations/{registrationId}/pass
GET /api/passes/{passId}
PUT /api/passes/{passId}/check-in
PUT /api/passes/{passId}/check-out
```

## Frontend class/component/function notes

This project uses React functional components instead of classes.

### App

`App.jsx`
- Defines all routes.
- Connects URLs to React pages.

### Navbar

`Navbar.jsx`
- Displays navigation.
- `handleLogout()` clears the logged-in user and navigates to login.

### EventCard

`EventCard.jsx`
- Displays one event.
- `formatDate()` formats Spring Boot LocalDateTime values.

### Login

`Login.jsx`
- `handleSubmit()` calls the login API and stores the returned user.

### Events

`Events.jsx`
- `loadEvents()` calls GET `/api/events`.
- `useEffect()` loads events when the page opens.

### EventDetails

`EventDetails.jsx`
- Loads event and time slots.
- `handleRegister()` creates a registration.
- `formatDate()` formats dates.

### CreateEvent

`CreateEvent.jsx`
- `handleChange()` updates form state.
- `handleSubmit()` creates an event.
- `toLocalDateTime()` converts HTML datetime input to Spring LocalDateTime format.

### MyEvents

`MyEvents.jsx`
- `loadEvents()` gets the organizer's events.
- `handleOpen()` opens an event.
- `handleClose()` closes an event.
- `handleDelete()` deletes an event.
- `handleViewRegistrations()` gets event registrations.

### MyRegistrations

`MyRegistrations.jsx`
- `loadRegistrations()` gets the user's registrations.
- `handleCancel()` cancels a registration.
- `handleGeneratePass()` creates a pass.

### RegistrationDetails

`RegistrationDetails.jsx`
- Loads one registration.
- `handleGeneratePass()` creates and opens its pass.

### PassDetails

`PassDetails.jsx`
- `loadPass()` gets a pass.
- `handleCheckIn()` checks the user into the event.
- `handleCheckOut()` checks the user out.

### Service files

The service layer keeps Axios calls separate from UI components:

- `api.js` - shared Axios client
- `authService.js` - login/logout/user
- `userService.js` - user API
- `eventService.js` - event APIs
- `timeSlotService.js` - time slot APIs
- `registrationService.js` - registration APIs
- `passService.js` - pass APIs

## Interview development order

Use this order while explaining the project:

1. Create React + Vite application.
2. Install Axios and React Router.
3. Create shared Axios client.
4. Create service files for each backend module.
5. Create Login page.
6. Create Events page.
7. Create Event Details + time slot selection.
8. Create registration flow.
9. Create My Registrations.
10. Create pass generation/view.
11. Add check-in/check-out.
12. Add Create Event and My Events for organizer.
13. Test the complete flow.

The implementation intentionally avoids Redux, complex state management, and unnecessary architecture because this is an interview/demo application.
