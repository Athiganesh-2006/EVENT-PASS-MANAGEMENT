import { Link } from 'react-router-dom'

// Displays the important information for one event.
export default function EventCard({ event }) {
  return (
    <div className="card">
      <span className={`badge ${String(event.status || '').toLowerCase()}`}>
        {event.status}
      </span>
      <h3>{event.title}</h3>
      <p>{event.description || 'No description available.'}</p>
      <p><strong>Venue:</strong> {event.venue}</p>
      <p><strong>Start:</strong> {formatDate(event.startTime)}</p>
      <p><strong>End:</strong> {formatDate(event.endTime)}</p>
      <Link className="button" to={`/events/${event.id}`}>View Details</Link>
    </div>
  )
}

// Converts an ISO date/time returned by Spring Boot into a readable value.
function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '-'
}