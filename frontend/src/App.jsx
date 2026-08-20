import { Link, Route, Routes } from 'react-router-dom';
import TaskListPage from './pages/TaskListPage.jsx';
import TaskFormPage from './pages/TaskFormPage.jsx';
import TaskDetailPage from './pages/TaskDetailPage.jsx';

function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <h1>AI Task Manager</h1>
          <p>Manage your tasks with priority, status, and due dates.</p>
        </div>
        <nav>
          <Link to="/">Tasks</Link>
          <Link to="/create">Create task</Link>
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<TaskListPage />} />
          <Route path="/create" element={<TaskFormPage />} />
          <Route path="/edit/:id" element={<TaskFormPage />} />
          <Route path="/tasks/:id" element={<TaskDetailPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
