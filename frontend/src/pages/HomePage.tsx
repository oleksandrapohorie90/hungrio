import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const HomePage: React.FC = () => {
  const username = localStorage.getItem('username');
  const location = useLocation() as any; // for optional flash message

  return (
    <div className="home-page" style={{ textAlign: 'center' }}>
      <h1>Welcome to Learning Management System</h1>

      {location.state?.message && (
        <div style={{ color: 'green', marginBottom: 12 }}>{location.state.message}</div>
      )}

      {username ? (
        <p>Hello, {username}! 🎉 Glad to see you back.</p>
      ) : (
        <>
          <p>Start your learning journey today!</p>
          <div className="cta-buttons" style={{ display: 'flex', gap: '12px', justifyContent: 'center' }}>
            <Link to="/register" className="btn btn-primary">Register</Link>
            <Link to="/login" className="btn btn-secondary">Login</Link>
          </div>
        </>
      )}
    </div>
  );
};

export default HomePage;
