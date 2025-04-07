import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./components/pages/menu/Home";
import { useState } from "react";
import { AuthProvider } from "./context/AuthProvider";
import UserPage from "./components/pages/user/User";
import "./App.css";
import "./App.scss";
import AuthenticationPage from "./components/pages/auth/Authentication";
import "react-datepicker/dist/react-datepicker.css";

function App() {
  const [userInfo, setUserInfo] = useState({
    info: {
      image: undefined,
      username: "TrongPhuc123",
      roles: [],
    },
    isAuth: true,
  });

  return (
    <AuthProvider value={userInfo}>
      <Router>
        <Routes>
          <Route path="*" element={<HomePage />} />
          <Route path="/user/*" element={<UserPage />} />
          <Route path="/auth/*" element={<AuthenticationPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
