import { useEffect, useState } from 'react'
import EventCard from '../components/EventCard'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'
import { getEvents } from '../services/eventService'

// Main event listing page.
export default function Events() {
  const [events, setEvents] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Loads all events from GET /api/events when the page opens.
  useEffect(() => {
    async function loadEvents() {
      try {
        setEvents(await getEvents())
      } catch (err) {
        setError(err.response?.data?.message || 'Could not load events')
      } finally {
        setLoading(false)
      }
    }

    loadEvents()
  }, [])

  if (loading) return <Loading />

  return (
    <section>
      <div className="page-header">
        <div>
          <h1>Events</h1>
          <p className="muted">Browse available events and register for a time slot.</p>
        </div>
      </div>

      <ErrorMessage error={error} />

      {events.length === 0 ? (
        <div className="card">No events found.</div>
      ) : (
        <div className="grid">
          {events.map((event) => <EventCard key={event.id} event={event} />)}
        </div>
      )}
    </section>
  )
}