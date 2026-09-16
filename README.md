# Event Pass Management System - Full Stack

Integrated application:
- Frontend: React + Vite + Axios
- Backend: Spring Boot + Spring Security + JWT
- Database: MySQL

## Run backend
1. Create a MySQL database named `eventpass`.
2. Configure environment variables or `src/main/resources/application.properties`:
   - `DB_URL=jdbc:mysql://localhost:3306/eventpass`
   - `DB_USERNAME=root`
   - `DB_PASSWORD=your_password`
   - `FRONTEND_URL=http://localhost:5173`
   - `JWT_SECRET=use-a-long-random-secret-at-least-32-characters`
3. Run `mvn spring-boot:run`.

## Run frontend
```bash
npm install
npm run dev
```

Frontend calls `http://localhost:8089/api` by default. Override with `.env`:
`VITE_API_URL=http://localhost:8089/api`

## Demo users
- admin@example.com / admin123
- organizer@example.com / organizer123
- participant@example.com / participant123

## Authentication
Login returns a JWT. React stores the token and Axios automatically sends:
`Authorization: Bearer <token>`

The backend validates the JWT and uses the role claim for Spring Security role-based authorization. The password is never stored in the JWT.

## Main flow
Participant: Login -> Events -> Event Details -> choose slot -> Register -> My Registrations -> Generate Pass -> Check In/Out.

Organizer/Admin: Login -> Create Event -> My Events -> Open/Close/Delete -> View Registrations -> create time slots.
