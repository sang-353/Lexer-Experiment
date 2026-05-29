import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export function getKeywords() {
  return api.get('/keywords')
}

export function addKeyword(keyword) {
  return api.post('/keywords', { keyword })
}

export function updateKeyword(oldKeyword, newKeyword) {
  return api.put('/keywords', { oldKeyword, newKeyword })
}

export function deleteKeyword(keyword) {
  return api.delete('/keywords', { data: { keyword } })
}

export function analyzeSource(sourceCode) {
  return api.post('/lexer/analyze', { sourceCode })
}
