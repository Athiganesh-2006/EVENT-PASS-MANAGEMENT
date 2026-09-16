// Shows a backend/API error in a consistent way.
export default function ErrorMessage({ error }) {
  if (!error) return null
  return <div className="error">{error}</div>
}