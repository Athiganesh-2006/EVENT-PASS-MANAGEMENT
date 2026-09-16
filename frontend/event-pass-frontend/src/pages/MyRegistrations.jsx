import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getCurrentUser } from '../services/authService'
import { cancelRegistration, getMyRegistrations } from '../services/registrationService'
import { createPass } from '../services/passService'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'

// Page showing registrations belonging to the current user.
export default function MyRegistrations() {
  const user = getCurrentUser()
  const [registrations, setRegistrations] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Loads GET /api/registrations/my?userId={id}.
  useEffect(() => {
    loadRegistrations()
  }, [])

  async function loadRegistrations() {
    if (!user) {
      setLoading(false)
      return
    }

    try {
      setRegistrations(await getMyRegistrations())
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load registrations')
    } finally {
      setLoading(false)
    }
  }

  // Cancels the selected registration.
  async function handleCancel(id) {
    try {
      await cancelRegistration(id)
      await loadRegistrations()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not cancel registration')
    }
  }

  // Generates a pass and navigates to the pass details page.
  async function handleGeneratePass(id) {
    try {
      const pass = await createPass(id)
      window.location.href = `/passes/${pass.id}`
    } catch (err) {
      setError(err.response?.data?.message || 'Could not generate pass')
    }
  }

  if (loading) return <Loading />

  if (!user) {
    return <div className="card">Please log in first.</div>
  }

  return (
    <section>
      <h1>My Registrations</h1>
      <ErrorMessage error={error} />

      {registrations.length === 0 ? (
        <div className="card">No registrations found.</div>
      ) : (
        <div className="list">
          {registrations.map((registration) => (
            <div className="card" key={registration.id}>
              <h3>Registration #{registration.id}</h3>
              <p><strong>Status:</strong> {registration.status}</p>
              <p><strong>Registered:</strong> {formatDate(registration.registeredAt)}</p>
              <div className="actions">
                <Link className="button secondary" to={`/registrations/${registration.id}`}>
                  Details
                </Link>
                {registration.status === 'REGISTERED' && (
                  <>
                    <button className="button" onClick={() => handleGeneratePass(registration.id)}>
                      Generate Pass
                    </button>
                    <button className="button danger" onClick={() => handleCancel(registration.id)}>
                      Cancel
                    </button>
                  </>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </section>
  )
}

// Formats a backend LocalDateTime value for display.
function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '-'
}