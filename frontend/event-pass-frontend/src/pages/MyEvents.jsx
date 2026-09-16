import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getCurrentUser } from '../services/authService'
import { closeEvent, deleteEvent, getMyEvents, openEvent } from '../services/eventService'
import { getEventRegistrations } from '../services/registrationService'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'

// Organizer page for viewing and managing events created by the current user.
export default function MyEvents() {
  const user = getCurrentUser()
  const [events, setEvents] = useState([])
  const [registrations, setRegistrations] = useState({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Loads events created by the current organizer.
  useEffect(() => {
    loadEvents()
  }, [])

  // Fetches GET /api/events/my.
  async function loadEvents() {
    if (!user) {
      setLoading(false)
      return
    }

    try {
      setEvents(await getMyEvents())
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load your events')
    } finally {
      setLoading(false)
    }
  }

  // Opens an event using PUT /api/events/{id}/open.
  async function handleOpen(id) {
    try {
      await openEvent(id)
      await loadEvents()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not open event')
    }
  }

  // Closes an event using PUT /api/events/{id}/close.
  async function handleClose(id) {
    try {
      await closeEvent(id)
      await loadEvents()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not close event')
    }
  }

  // Deletes an event using DELETE /api/events/{id}.
  async function handleDelete(id) {
    if (!window.confirm('Delete this event?')) return

    try {
      await deleteEvent(id)
      await loadEvents()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not delete event')
    }
  }

  // Loads registrations for one selected event.
  async function handleViewRegistrations(id) {
    try {
      const data = await getEventRegistrations(id)
      setRegistrations((old) => ({ ...old, [id]: data }))
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load registrations')
    }
  }

  if (loading) return <Loading />

  if (!user) {
    return <div className="card">Please <Link to="/login">login</Link> as an organizer.</div>
  }

  if (!['ORGANIZER', 'ADMIN'].includes(user.role)) {
    return <div className="card">Only organizers and admins can manage events.</div>
  }

  return (
    <section>
      <h1>My Events</h1>
      <ErrorMessage error={error} />

      {events.length === 0 ? (
        <div className="card">You have not created any events.</div>
      ) : (
        <div className="list">
          {events.map((event) => (
            <div className="card" key={event.id}>
              <span className={`badge ${String(event.status || '').toLowerCase()}`}>
                {event.status}
              </span>
              <h3>{event.title}</h3>
              <p>{event.venue}</p>

              <div className="actions">
                <Link className="button secondary" to={`/events/${event.id}`}>View</Link>
                <button className="button" onClick={() => handleOpen(event.id)}>Open</button>
                <button className="button secondary" onClick={() => handleClose(event.id)}>Close</button>
                <button className="button danger" onClick={() => handleDelete(event.id)}>Delete</button>
                <button className="button secondary" onClick={() => handleViewRegistrations(event.id)}>
                  Registrations
                </button>
              </div>

              {registrations[event.id] && (
                <div className="registration-list">
                  <h4>Registrations</h4>
                  {registrations[event.id].length === 0 ? (
                    <p className="muted">No registrations.</p>
                  ) : (
                    registrations[event.id].map((registration) => (
                      <div className="mini-row" key={registration.id}>
                        <span>Registration #{registration.id}</span>
                        <span>{registration.status}</span>
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </section>
  )
}