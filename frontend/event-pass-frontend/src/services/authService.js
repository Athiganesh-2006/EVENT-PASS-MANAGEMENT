import api from './api'

export async function login(email, password) {
  const response = await api.post('/auth/login', { email, password })
  const { token, ...user } = response.data
  localStorage.setItem('eventToken', token)
  localStorage.setItem('eventUser', JSON.stringify(user))
  return user
}

export function logout() {
  localStorage.removeItem('eventToken')
  localStorage.removeItem('eventUser')
}

export function getCurrentUser() {
  return JSON.parse(localStorage.getItem('eventUser') || 'null')
}

export function getToken() { return localStorage.getItem('eventToken') }

export function isLoggedIn() { return Boolean(getToken()) }

export function hasRole(...roles) {
  const user = getCurrentUser()
  return Boolean(user && roles.includes(user.role))
}
