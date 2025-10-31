import React from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import ChatPage from "./pages/ChatPage";
import NavBar from "./components/NavBar";
import { AuthProvider } from "./context/AuthContext";  // ⬅️ add this

// keep the simple inline guard
const ProtectedRoute: React.FC<{ children: React.ReactNode; requireAuth?: boolean }> = ({ children, requireAuth = true }) => {
  const isAuthenticated = localStorage.getItem("token") !== null;
  if (requireAuth && !isAuthenticated) {
    return <Navigate to="/login" replace state={{ msg: "You must be logged in to access the chat." }} />;
  }
  if (!requireAuth && isAuthenticated) {
    return <Navigate to="/" replace />;
  }
  return <>{children}</>;
};

function App() {
  return (
    <AuthProvider>   {/* ⬅️ wrap everything */}
      <Router>
        <div className="App">
          <NavBar />
          <main className="container">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/register" element={<ProtectedRoute requireAuth={false}><RegisterPage /></ProtectedRoute>} />
              <Route path="/login" element={<ProtectedRoute requireAuth={false}><LoginPage /></ProtectedRoute>} />
              <Route path="/chat" element={<ProtectedRoute><ChatPage /></ProtectedRoute>} />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}
export default App;




// import React from 'react';
// import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
// import HomePage from './pages/HomePage';
// import RegisterPage from './pages/RegisterPage';
// import LoginPage from './pages/LoginPage';
// import ChatPage from './pages/ChatPage';
// import NavBar from './components/NavBar';

// interface ProtectedRouteProps {
//   children: React.ReactNode;
//   requireAuth?: boolean;
// }

// const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requireAuth = true }) => {
//   const isAuthenticated = localStorage.getItem('token') !== null;

//   if (requireAuth && !isAuthenticated) {
//     // Send a message so LoginPage can show it
//     return <Navigate to="/login" replace state={{ msg: 'You must be logged in to access the chat.' }} />;
//   }

//   if (!requireAuth && isAuthenticated) {
//     return <Navigate to="/" replace />;
//   }

//   return <>{children}</>;
// };

// function App() {
//   return (
//     <Router>
//       <div className="App">
//         <NavBar />
//         <main className="container">
//           <Routes>
//             <Route path="/" element={<HomePage />} />

//             <Route
//               path="/register"
//               element={
//                 <ProtectedRoute requireAuth={false}>
//                   <RegisterPage />
//                 </ProtectedRoute>
//               }
//             />

//             <Route
//               path="/login"
//               element={
//                 <ProtectedRoute requireAuth={false}>
//                   <LoginPage />
//                 </ProtectedRoute>
//               }
//             />

//             <Route
//               path="/chat"
//               element={
//                 <ProtectedRoute>
//                   <ChatPage />
//                 </ProtectedRoute>
//               }
//             />
//           </Routes>
//         </main>
//       </div>
//     </Router>
//   );
// }

// export default App;
