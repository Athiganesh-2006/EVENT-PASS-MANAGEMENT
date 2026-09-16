import api from './api'

export async function createRegistration(eventId, timeSlotId) {
  return (await api.post('/registrations', { eventId, timeSlotId })).data
}
export async function getMyRegistrations() { return (await api.get('/registrations/my')).data }
export async function getRegistration(registrationId) { return (await api.get(`/registrations/${registrationId}`)).data }
export async function cancelRegistration(registrationId) { return (await api.put(`/registrations/${registrationId}/cancel`)).data }
export async function getEventRegistrations(eventId) { return (await api.get(`/events/${eventId}/registrations`)).data }
