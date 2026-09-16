import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../services/authService'
import ErrorMessage from '../components/ErrorMessage'

// Login page for the Event Pass application.
export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  // Sends the login form to POST /api/auth/login.
  async function handleSubmit(event) {
    event.preventDefault()
    setError('')

    try {
      await login(email, password)
      navigate('/events')
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password')
    }
  }

  return (
    <div className="form-page narrow">
      <h1>Login</h1>
      <p className="muted">Sign in to manage event registrations and passes.</p>
      <ErrorMessage error={error} />

      <form onSubmit={handleSubmit} className="form card">
        <label>Email</label>
        <input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          type="email"
          placeholder="Enter email"
          required
        />

        <label>Password</label>
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
          placeholder="Enter password"
          required
        />

        <button className="button" type="submit">Login</button>
      </form>
    </div>
  )
}