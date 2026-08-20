import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { deleteTask, fetchTasks } from '../api/tasks.js';

const statuses = ['', 'TODO', 'IN_PROGRESS', 'COMPLETED'];
const priorities = ['', 'LOW', 'MEDIUM', 'HIGH'];

function TaskListPage() {
  const [tasks, setTasks] = useState([]);
  const [filters, setFilters] = useState({ status: '', priority: '' });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadTasks();
  }, [filters]);

  async function loadTasks() {
    setLoading(true);
    setError(null);

    try {
      const params = {};
      if (filters.status) {
        params.status = filters.status;
      }
      if (filters.priority) {
        params.priority = filters.priority;
      }
      const response = await fetchTasks(params);
      setTasks(response.data);
    } catch (fetchError) {
      setError('Unable to load tasks. Make sure the backend is running.');
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this task?')) {
      return;
    }

    try {
      await deleteTask(id);
      setTasks((current) => current.filter((task) => task.id !== id));
    } catch (deleteError) {
      setError('Unable to delete the task.');
    }
  }

  return (
    <section className="page-container">
      <div className="page-header">
        <h2>Tasks</h2>
        <Link className="button button-secondary" to="/create">
          New task
        </Link>
      </div>

      <div className="filters">
        <label>
          Status
          <select
            value={filters.status}
            onChange={(event) => setFilters({ ...filters, status: event.target.value })}
          >
            {statuses.map((option) => (
              <option key={option} value={option}>
                {option || 'Any'}
              </option>
            ))}
          </select>
        </label>
        <label>
          Priority
          <select
            value={filters.priority}
            onChange={(event) => setFilters({ ...filters, priority: event.target.value })}
          >
            {priorities.map((option) => (
              <option key={option} value={option}>
                {option || 'Any'}
              </option>
            ))}
          </select>
        </label>
      </div>

      {error && <div className="alert">{error}</div>}
      {loading ? (
        <p>Loading tasks…</p>
      ) : (
        <div className="task-list">
          {tasks.length === 0 ? (
            <p>No tasks found. Create one to get started.</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Status</th>
                  <th>Priority</th>
                  <th>Due date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tasks.map((task) => (
                  <tr key={task.id}>
                    <td>
                      <Link to={`/tasks/${task.id}`}>{task.title}</Link>
                    </td>
                    <td>{task.status}</td>
                    <td>{task.priority}</td>
                    <td>{task.dueDate || '–'}</td>
                    <td className="task-actions">
                      <Link className="button button-small" to={`/edit/${task.id}`}>
                        Edit
                      </Link>
                      <button className="button button-danger button-small" onClick={() => handleDelete(task.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </section>
  );
}

export default TaskListPage;
