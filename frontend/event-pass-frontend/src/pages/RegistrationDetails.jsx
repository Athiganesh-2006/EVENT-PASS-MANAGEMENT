import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getRegistration } from '../services/registrationService'
import { createPass } from '../services/passService'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'

// Shows one registration and allows pass generation.
export default function RegistrationDetails() {
  const { registrationId } = useParams()
  const navigate = useNavigate()
  const [registration, setRegistration] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Loads GET /api/registrations/{registrationId}.
  useEffect(() => {
    async function loadRegistration() {
      try {
        setRegistration(await getRegistration(registrationId))
      } catch (err) {
        setError(err.response?.data?.message || 'Could not load registration')
      } finally {
        setLoading(false)
      }
    }

    loadRegistration()
  }, [registrationId])

  // Generates a pass for this registration.
  async function handleGeneratePass() {
    try {
      const pass = await createPass(registrationId)
      navigate(`/passes/${pass.id}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not generate pass')
    }
  }

  if (loading) return <Loading />
  if (!registration) return <ErrorMessage error={error || 'Registration not found'} />

  return (
    <section className="narrow">
      <ErrorMessage error={error} />

      <div className="card">
        <h1>Registration #{registration.id}</h1>
        <p><strong>Status:</strong> {registration.status}</p>
        <p><strong>Registered:</strong> {formatDate(registration.registeredAt)}</p>
        <p><strong>Event ID:</strong> {registration.eventId ?? '-'}</p>
        <p><strong>Time Slot ID:</strong> {registration.timeSlotId ?? '-'}</p>

        {registration.status === 'REGISTERED' && (
          <button className="button" onClick={handleGeneratePass}>
            Generate Pass
          </button>
        )}
      </div>

      <Link className="text-link" to="/registrations">← Back to registrations</Link>
    </section>
  )
}

// Formats a backend LocalDateTime value for display.
function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '-'
}