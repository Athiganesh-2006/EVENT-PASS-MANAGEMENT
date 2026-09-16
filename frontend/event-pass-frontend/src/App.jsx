import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import Login from './pages/Login'
import Events from './pages/Events'
import EventDetails from './pages/EventDetails'
import CreateEvent from './pages/CreateEvent'
import MyEvents from './pages/MyEvents'
import MyRegistrations from './pages/MyRegistrations'
import RegistrationDetails from './pages/RegistrationDetails'
import PassDetails from './pages/PassDetails'
import { getCurrentUser } from './services/authService'

function RequireAuth({ children, roles }) {
  const user = getCurrentUser()
  if (!user) return <Navigate to="/login" replace />
  if (roles && !roles.includes(user.role)) return <Navigate to="/events" replace />
  return children
}

export default function App() {
  return (
    <>
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<Navigate to="/events" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/events" element={<Events />} />
          <Route path="/events/:eventId" element={<EventDetails />} />
          <Route path="/create-event" element={<RequireAuth roles={['ORGANIZER', 'ADMIN']}><CreateEvent /></RequireAuth>} />
          <Route path="/my-events" element={<RequireAuth roles={['ORGANIZER', 'ADMIN']}><MyEvents /></RequireAuth>} />
          <Route path="/registrations" element={<RequireAuth roles={['PARTICIPANT']}><MyRegistrations /></RequireAuth>} />
          <Route path="/registrations/:registrationId" element={<RequireAuth><RegistrationDetails /></RequireAuth>} />
          <Route path="/passes/:passId" element={<RequireAuth><PassDetails /></RequireAuth>} />
        </Routes>
      </main>
    </>
  )
}
