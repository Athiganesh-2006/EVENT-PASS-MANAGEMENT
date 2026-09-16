import api from './api'

// GET /api/users/{id}
export async function getUser(id) {
  const response = await api.get(`/users/${id}`)
  return response.data
}