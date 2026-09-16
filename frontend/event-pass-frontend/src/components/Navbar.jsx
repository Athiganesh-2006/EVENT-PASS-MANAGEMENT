import { Link, useNavigate } from 'react-router-dom'
import { getCurrentUser, logout } from '../services/authService'

export default function Navbar() {
  const navigate = useNavigate()
  const user = getCurrentUser()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  const canManageEvents = user && ['ORGANIZER', 'ADMIN'].includes(user.role)

  return (
    <nav className="navbar">
      <div className="nav-inner">
        <Link className="brand" to="/events">EventPass</Link>
        <div className="nav-links">
          <Link to="/events">Events</Link>
          {user?.role === 'PARTICIPANT' && <Link to="/registrations">My Registrations</Link>}
          {canManageEvents && <Link to="/my-events">My Events</Link>}
          {canManageEvents && <Link to="/create-event">Create Event</Link>}
          {user ? (
            <>
              <span className="muted">{user.name} ({user.role})</span>
              <button className="link-button" onClick={handleLogout}>Logout</button>
            </>
          ) : <Link to="/login">Login</Link>}
        </div>
      </div>
    </nav>
  )
}
