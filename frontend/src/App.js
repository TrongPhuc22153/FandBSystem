import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomeComponent from './components/pages/menu/Home';
import { useState } from 'react';
import { AuthProvider } from './context/AuthProvider';
import UserComponent from './components/pages/user/User';
import './App.css';
import './App.scss'

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
          <Route path='/user' element={<UserComponent/>}/>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
