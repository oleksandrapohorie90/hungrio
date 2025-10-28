import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";

type LocationState =
  | {
      from?: { pathname?: string };
      message?: string;
      msg?: string; // from ProtectedRoute
    }
  | undefined;

interface LoginFormData {
  username: string;
  password: string;
}

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as LocationState;

  const [formData, setFormData] = useState<LoginFormData>({
    username: "",
    password: "",
  });
  const [error, setError] = useState<string>("");
  const [info, setInfo] = useState<string>("");
  const [loading, setLoading] = useState(false);

  // Show message passed from redirects (e.g., ProtectedRoute)
  useEffect(() => {
    if (state?.msg || state?.message) {
      setInfo(state.msg || state.message || "");
    }
  }, [state]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setInfo("");
    setLoading(true);

    try {
      // If your backend runs on the same origin via a proxy, '/api/auth/login' is fine.
      // Otherwise, use 'http://localhost:8080/api/auth/login'
      const res = await axios.post("/api/auth/login", formData);

      // Expecting { token: string, username?: string }
      const token: string | undefined = res.data?.token;
      if (!token) {
        throw new Error("Login succeeded but no token was returned.");
      }

      localStorage.setItem("token", token);
      localStorage.setItem("username", res.data?.username || formData.username);

      // Redirect back to where the user came from or to home/chat
      const redirectTo =
        state?.from?.pathname && state.from.pathname !== "/login"
          ? state.from.pathname
          : "/chat"; // or "/" if you prefer
      navigate(redirectTo, {
        state: { message: `Welcome, ${res.data?.username || formData.username}!` },
        replace: true,
      });
    } catch (err: any) {
      const apiMsg =
        err?.response?.data?.message ||
        err?.response?.data ||
        err?.message ||
        "An error occurred during login";
      setError(typeof apiMsg === "string" ? apiMsg : "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page" style={{ maxWidth: 420, margin: "3rem auto" }}>
      <h2>Login</h2>

      {info && (
        <div className="alert alert-info" style={{ marginBottom: 12 }}>
          {info}
        </div>
      )}

      {error && (
        <div className="alert alert-danger" style={{ marginBottom: 12, color: "#b00020" }}>
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group" style={{ marginBottom: 12 }}>
          <label htmlFor="username">Username:</label>
          <input
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            className="form-control"
            placeholder="Enter your username"
            required
            autoComplete="username"
            style={{ width: "100%", padding: 8 }}
          />
        </div>

        <div className="form-group" style={{ marginBottom: 16 }}>
          <label htmlFor="password">Password:</label>
          <input
            id="password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            className="form-control"
            placeholder="Enter your password"
            required
            autoComplete="current-password"
            style={{ width: "100%", padding: 8 }}
          />
        </div>

        <button
          type="submit"
          className="btn btn-primary"
          disabled={loading}
          style={{ width: "100%", padding: "10px 0" }}
        >
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>
    </div>
  );
};

export default LoginPage;



// import React, { useState } from 'react';
// import { useNavigate, useLocation } from 'react-router-dom';
// import axios from 'axios';

// interface LocationState {
//   message?: string;
// }

// interface LoginFormData {
//   username: string;
//   password: string;
// }

// const LoginPage: React.FC = () => {
//   const navigate = useNavigate();
//   const location = useLocation();
//   const state = location.state as LocationState;

//   const [formData, setFormData] = useState<LoginFormData>({
//     username: '',
//     password: ''
//   });
//   const [error, setError] = useState<string>('');

//   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     const { name, value } = e.target;
//     setFormData(prev => ({
//       ...prev,
//       [name]: value
//     }));
//   };

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setError('');
//     try {
//       await axios.post('/api/auth/login', formData);
    
//       //Save username so NavBar/HomePage can greet the user
//       localStorage.setItem('username', formData.username);
    
//       //Optionally send a success message back to homepage
//       navigate('/', { state: { message: `Welcome, ${formData.username}!` } });
//     } catch (error: any) {
//       setError(error.response?.data || 'An error occurred during login');
//     }
//   };

//   return (
//     <div className="login-page">
//       <h2>Login</h2>
//       {state?.message && (
//         <div className="alert alert-success">
//           {state.message}
//         </div>
//       )}
//       {error && (
//         <div className="alert alert-danger">
//           {error}
//         </div>
//       )}
//       <form onSubmit={handleSubmit}>
//         <div className="form-group">
//           <label htmlFor="username">Username:</label>
//           <input
//             type="text"
//             id="username"
//             name="username"
//             value={formData.username}
//             onChange={handleChange}
//             className="form-control"
//             required
//           />
//         </div>
//         <div className="form-group">
//           <label htmlFor="password">Password:</label>
//           <input
//             type="password"
//             id="password"
//             name="password"
//             value={formData.password}
//             onChange={handleChange}
//             className="form-control"
//             required
//           />
//         </div>
//         <button type="submit" className="btn btn-primary">Login</button>
//       </form>
//     </div>
//   );
// };

// export default LoginPage;