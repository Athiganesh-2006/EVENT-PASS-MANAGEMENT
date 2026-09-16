import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { checkIn, checkOut, getPass } from '../services/passService'
import Loading from '../components/Loading'
import ErrorMessage from '../components/ErrorMessage'

// Displays a generated pass and provides check-in/check-out actions.
export default function PassDetails() {
  const { passId } = useParams()
  const [pass, setPass] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Loads GET /api/passes/{passId}.
  useEffect(() => {
    loadPass()
  }, [passId])

  async function loadPass() {
    try {
      setPass(await getPass(passId))
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load pass')
    } finally {
      setLoading(false)
    }
  }

  // Calls PUT /api/passes/{passId}/check-in.
  async function handleCheckIn() {
    try {
      setPass(await checkIn(passId))
    } catch (err) {
      setError(err.response?.data?.message || 'Check-in failed')
    }
  }

  // Calls PUT /api/passes/{passId}/check-out.
  async function handleCheckOut() {
    try {
      setPass(await checkOut(passId))
    } catch (err) {
      setError(err.response?.data?.message || 'Check-out failed')
    }
  }

  if (loading) return <Loading />
  if (!pass) return <ErrorMessage error={error || 'Pass not found'} />

  return (
    <section className="narrow">
      <ErrorMessage error={error} />

      <div className="pass-card">
        <p className="pass-label">EVENT PASS</p>
        <h1>{pass.passCode}</h1>

        <div className="pass-info">
          <div><span>Status</span><strong>{pass.status}</strong></div>
          <div><span>Registration</span><strong>#{pass.registrationId ?? '-'}</strong></div>
          <div><span>Check-in</span><strong>{formatDate(pass.checkInAt)}</strong></div>
          <div><span>Check-out</span><strong>{formatDate(pass.checkOutAt)}</strong></div>
        </div>

        <div className="actions">
          {pass.status === 'ACTIVE' && (
            <button className="button" onClick={handleCheckIn}>Check In</button>
          )}

          {pass.status === 'CHECKED_IN' && (
            <button className="button" onClick={handleCheckOut}>Check Out</button>
          )}
        </div>
      </div>
    </section>
  )
}

// Formats a backend LocalDateTime value for display.
function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '-'
}