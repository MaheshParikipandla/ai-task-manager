import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { createTask, fetchTaskById, updateTask } from '../api/tasks.js';

const statusOptions = ['TODO', 'IN_PROGRESS', 'COMPLETED'];
const priorityOptions = ['LOW', 'MEDIUM', 'HIGH'];

function TaskFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    title: '',
    description: '',
    status: 'TODO',
    priority: 'MEDIUM',
    dueDate: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    if (id) {
      loadTask();
    }
  }, [id]);

  async function loadTask() {
    setLoading(true);
    setError(null);

    try {
      const response = await fetchTaskById(id);
      const task = response.data;
      setForm({
        title: task.title,
        description: task.description || '',
        status: task.status,
        priority: task.priority,
        dueDate: task.dueDate || '',
      });
    } catch (loadError) {
      setError('Unable to load task for editing.');
    } finally {
      setLoading(false);
    }
  }

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError(null);
    setIsSaving(true);

    if (!form.title.trim()) {
      setError('Title is required.');
      setIsSaving(false);
      return;
    }

    try {
      if (id) {
        await updateTask(id, form);
      } else {
        await createTask(form);
      }
      navigate('/');
    } catch (saveError) {
      setError('Unable to save task. Confirm the backend is running and input is valid.');
    } finally {
      setIsSaving(false);
    }
  }

  return (
    <section className="page-container">
      <div className="page-header">
        <h2>{id ? 'Edit task' : 'Create task'}</h2>
      </div>

      {error && <div className="alert">{error}</div>}
      {loading ? (
        <p>Loading task…</p>
      ) : (
        <form className="task-form" onSubmit={handleSubmit}>
          <label>
            Title
            <input
              value={form.title}
              onChange={(event) => updateField('title', event.target.value)}
              required
            />
          </label>

          <label>
            Description
            <textarea
              rows="4"
              value={form.description}
              onChange={(event) => updateField('description', event.target.value)}
            />
          </label>

          <div className="field-row">
            <label>
              Status
              <select value={form.status} onChange={(event) => updateField('status', event.target.value)}>
                {statusOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Priority
              <select value={form.priority} onChange={(event) => updateField('priority', event.target.value)}>
                {priorityOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
            </label>
          </div>

          <label>
            Due date
            <input
              type="date"
              value={form.dueDate}
              onChange={(event) => updateField('dueDate', event.target.value)}
            />
          </label>

          <div className="form-actions">
            <button className="button button-primary" type="submit" disabled={isSaving}>
              {isSaving ? 'Saving…' : id ? 'Update task' : 'Create task'}
            </button>
            <button className="button button-secondary" type="button" onClick={() => navigate('/')}>Cancel</button>
          </div>
        </form>
      )}
    </section>
  );
}

export default TaskFormPage;
