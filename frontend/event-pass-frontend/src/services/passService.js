import api from './api'

// POST /api/registrations/{registrationId}/pass
export async function createPass(registrationId) {
  const response = await api.post(`/registrations/${registrationId}/pass`)
  return response.data
}

// GET /api/passes/{passId}
export async function getPass(passId) {
  const response = await api.get(`/passes/${passId}`)
  return response.data
}

// PUT /api/passes/{passId}/check-in
export async function checkIn(passId) {
  const response = await api.put(`/passes/${passId}/check-in`)
  return response.data
}

// PUT /api/passes/{passId}/check-out
export async function checkOut(passId) {
  const response = await api.put(`/passes/${passId}/check-out`)
  return response.data
}