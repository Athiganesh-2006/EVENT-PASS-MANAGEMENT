import api from './api'

export async function getEvents() { return (await api.get('/events')).data }
export async function getEvent(eventId) { return (await api.get(`/events/${eventId}`)).data }
export async function createEvent(event) { return (await api.post('/events', event)).data }
export async function getMyEvents() { return (await api.get('/events/my')).data }
export async function openEvent(eventId) { return (await api.put(`/events/${eventId}/open`)).data }
export async function closeEvent(eventId) { return (await api.put(`/events/${eventId}/close`)).data }
export async function deleteEvent(eventId) { await api.delete(`/events/${eventId}`) }
