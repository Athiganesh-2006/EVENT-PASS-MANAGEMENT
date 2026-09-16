import api from './api'

export async function getTimeSlots(eventId) { return (await api.get(`/events/${eventId}/timeslots`)).data }
export async function getTimeSlot(slotId) { return (await api.get(`/timeslots/${slotId}`)).data }
export async function createTimeSlot(eventId, data) { return (await api.post(`/events/${eventId}/timeslots`, data)).data }
