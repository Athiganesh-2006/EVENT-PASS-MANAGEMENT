import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getEvent } from '../services/eventService'
import { getTimeSlots } from '../services/timeSlotService'
import { createRegistration } from '../services/registrationService'
import { getCurrentUser } from '../services/authService'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'

// Event details page.
// It shows the event, its time slots, and allows a logged-in user to register.
export default function EventDetails() {
  const { eventId } = useParams()
  const navigate = useNavigate()
  const user = getCurrentUser()

  const [event, setEvent] = useState(null)
  const [slots, setSlots] = useState([])
  const [selectedSlot, setSelectedSlot] = useState('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  // Loads the selected event and its time slots.
  useEffect(() => {
    async function loadData() {
      try {
        const [eventData, slotData] = await Promise.all([
          getEvent(eventId),
          getTimeSlots(eventId)
        ])
        setEvent(eventData)
        setSlots(slotData)
      } catch (err) {
        setError(err.response?.data?.message || 'Could not load event')
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [eventId])

  // Creates a registration for the current user and selected time slot.
  async function handleRegister() {
    if (!user) {
      navigate('/login')
      return
    }

    if (!selectedSlot) {
      setError('Please select a time slot.')
      return
    }

    setSaving(true)
    setError('')

    try {
      const registration = await createRegistration(
        Number(eventId),
        Number(selectedSlot)
      )
      navigate(`/registrations/${registration.id}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed')
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <Loading />
  if (!event) return <ErrorMessage error={error || 'Event not found'} />

  return (
    <section>
      <ErrorMessage error={error} />

      <div className="card">
        <span className={`badge ${String(event.status || '').toLowerCase()}`}>
          {event.status}
        </span>
        <h1>{event.title}</h1>
        <p>{event.description}</p>
        <p><strong>Venue:</strong> {event.venue}</p>
        <p><strong>Start:</strong> {formatDate(event.startTime)}</p>
        <p><strong>End:</strong> {formatDate(event.endTime)}</p>
        <p><strong>Capacity:</strong> {event.capacity}</p>
      </div>

      <div className="card section-gap">
        <h2>Choose a Time Slot</h2>

        {slots.length === 0 ? (
          <p className="muted">No time slots available.</p>
        ) : (
          <div className="slot-list">
            {slots.map((slot) => (
              <label className="slot" key={slot.id}>
                <input
                  type="radio"
                  name="timeSlot"
                  value={slot.id}
                  checked={String(selectedSlot) === String(slot.id)}
                  onChange={(e) => setSelectedSlot(e.target.value)}
                />
                <span>
                  {formatDate(slot.startTime)} - {formatDate(slot.endTime)}
                  <small>Capacity: {slot.capacity}</small>
                </span>
              </label>
            ))}
          </div>
        )}

        <button
          className="button"
          disabled={!selectedSlot || saving || event.status !== 'OPEN'}
          onClick={handleRegister}
        >
          {saving ? 'Registering...' : 'Register for Event'}
        </button>

        {event.status !== 'OPEN' && (
          <p className="muted">Registration is available only when the event is OPEN.</p>
        )}
      </div>
    </section>
  )
}

// Formats a backend LocalDateTime value for display.
function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '-'
}