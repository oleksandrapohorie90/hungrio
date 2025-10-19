import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

const NavBar: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [username, setUsername] = useState<string | null>(localStorage.getItem('username'));

  // When route changes (e.g., after login redirect), re-read localStorage
  useEffect(() => {
    setUsername(localStorage.getItem('username'));
  }, [location.pathname]);

  const handleLogout = () => {
    localStorage.removeItem('username');
    setUsername(null);
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="container">
        <Link to="/" className="navbar-brand">Learning Management System</Link>

        <div className="nav-links">
          {username ? (
            <>
              <span className="welcome-text">Welcome, {username}!</span>
              <button onClick={handleLogout} className="btn btn-link">Logout</button>
            </>
          ) : (
            <>
              {location.pathname !== '/register' && <Link to="/register" className="nav-link">Register</Link>}
              {location.pathname !== '/login' && <Link to="/login" className="nav-link">Login</Link>}
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
