import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createEvent } from '../services/eventService'
import { getCurrentUser } from '../services/authService'
import ErrorMessage from '../components/ErrorMessage'

// Simple event creation form for an organizer.
export default function CreateEvent() {
  const user = getCurrentUser()
  const navigate = useNavigate()

  const [form, setForm] = useState({
    title: '',
    description: '',
    eventType: 'CONFERENCE',
    venue: '',
    startTime: '',
    endTime: '',
    capacity: ''
  })
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  // Updates one form field without creating separate state for every input.
  function handleChange(event) {
    setForm({
      ...form,
      [event.target.name]: event.target.value
    })
  }

  // Sends the form to POST /api/events.
  async function handleSubmit(event) {
    event.preventDefault()

    if (!user) {
      navigate('/login')
      return
    }

    setSaving(true)
    setError('')

    try {
      const created = await createEvent({
        ...form,
        capacity: Number(form.capacity),
        startTime: toLocalDateTime(form.startTime),
        endTime: toLocalDateTime(form.endTime)
      })

      navigate(`/events/${created.id}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create event')
    } finally {
      setSaving(false)
    }
  }

  return (
    <section className="form-page">
      <h1>Create Event</h1>
      <p className="muted">Create a basic event and manage it from My Events.</p>
      <ErrorMessage error={error} />

      <form className="form card" onSubmit={handleSubmit}>
        <label>Title</label>
        <input name="title" value={form.title} onChange={handleChange} required />

        <label>Description</label>
        <textarea name="description" value={form.description} onChange={handleChange} rows="4" />

        <label>Event Type</label>
        <input name="eventType" value={form.eventType} onChange={handleChange} required />

        <label>Venue</label>
        <input name="venue" value={form.venue} onChange={handleChange} required />

        <label>Start Time</label>
        <input name="startTime" type="datetime-local" value={form.startTime} onChange={handleChange} required />

        <label>End Time</label>
        <input name="endTime" type="datetime-local" value={form.endTime} onChange={handleChange} required />

        <label>Capacity</label>
        <input name="capacity" type="number" min="1" value={form.capacity} onChange={handleChange} required />

        <button className="button" disabled={saving}>
          {saving ? 'Creating...' : 'Create Event'}
        </button>
      </form>
    </section>
  )
}

// Converts HTML datetime-local value to the LocalDateTime string expected by Spring Boot.
function toLocalDateTime(value) {
  return value ? `${value}:00` : value
}