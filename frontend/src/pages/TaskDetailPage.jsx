import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchTaskById, updateTaskStatus } from '../api/tasks.js';

const statuses = ['TODO', 'IN_PROGRESS', 'COMPLETED'];

function TaskDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [task, setTask] = useState(null);
  const [error, setError] = useState(null);
  const [status, setStatus] = useState('TODO');
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadTask();
  }, [id]);

  async function loadTask() {
    setLoading(true);
    setError(null);

    try {
      const response = await fetchTaskById(id);
      setTask(response.data);
      setStatus(response.data.status);
    } catch (loadError) {
      setError('Unable to load task details.');
    } finally {
      setLoading(false);
    }
  }

  async function handleStatusChange(event) {
    const nextStatus = event.target.value;
    setStatus(nextStatus);

    setSaving(true);
    try {
      const response = await updateTaskStatus(id, nextStatus);
      setTask(response.data);
    } catch (saveError) {
      setError('Unable to update task status.');
    } finally {
      setSaving(false);
    }
  }

  return (
    <section className="page-container">
      <div className="page-header">
        <h2>Task details</h2>
      </div>

      {error && <div className="alert">{error}</div>}
      {loading ? (
        <p>Loading task…</p>
      ) : task ? (
        <article className="task-detail">
          <div className="task-meta">
            <p>
              <strong>Status:</strong>
              <select value={status} onChange={handleStatusChange} disabled={saving}>
                {statuses.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
            </p>
            <p>
              <strong>Priority:</strong> {task.priority}
            </p>
            <p>
              <strong>Due date:</strong> {task.dueDate || 'No due date'}
            </p>
            <p>
              <strong>Created:</strong> {new Date(task.createdAt).toLocaleString()}
            </p>
            <p>
              <strong>Updated:</strong> {new Date(task.updatedAt).toLocaleString()}
            </p>
          </div>

          <div className="task-body">
            <h3>{task.title}</h3>
            <p>{task.description || 'No description provided.'}</p>
          </div>

          <div className="form-actions">
            <button className="button button-secondary" type="button" onClick={() => navigate('/')}>Back</button>
            <button className="button button-primary" type="button" onClick={() => navigate(`/edit/${task.id}`)}>
              Edit task
            </button>
          </div>
        </article>
      ) : (
        <p>No task found.</p>
      )}
    </section>
  );
}

export default TaskDetailPage;
