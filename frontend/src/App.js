import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomeComponent from './components/pages/Home';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='*' element={<HomeComponent/>}/>
      </Routes>
    </Router>
  );
}

export default App;
