import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomeComponent from './components/pages/menu/Home';
import { useState } from 'react';
import { AuthProvider } from './context/AuthProvider';

function App() {
  const [userInfo, setUserInfo] = useState({
    info: {
      image: undefined,
      username: 'TrongPhuc123',
      roles: []
    },
    isAuth: true
  })


  return (
    <AuthProvider value={userInfo}>
      <Router>
        <Routes>
          <Route path='*' element={<HomeComponent/>}/>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
