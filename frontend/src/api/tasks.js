import axios from 'axios';

const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
const client = axios.create({ baseURL: baseUrl, headers: { 'Content-Type': 'application/json' } });

export function fetchTasks(filters = {}) {
  return client.get('/tasks', { params: filters });
}

export function fetchTaskById(taskId) {
  return client.get(`/tasks/${taskId}`);
}

export function createTask(task) {
  return client.post('/tasks', task);
}

export function updateTask(taskId, task) {
  return client.put(`/tasks/${taskId}`, task);
}

export function updateTaskStatus(taskId, status) {
  return client.patch(`/tasks/${taskId}/status`, { status });
}

export function deleteTask(taskId) {
  return client.delete(`/tasks/${taskId}`);
}
