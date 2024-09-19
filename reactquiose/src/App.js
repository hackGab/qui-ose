import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Formulaire from "./components/Formulaire";
import './App.css';

function App() {
  return (
      <Router>
          <div className='App-body'>
              <Routes>
                    <Route path='/' element={<Navigate to='/login' />}/>
                    <Route path='/signUp' element={<Formulaire title="Inscription"/>}/>
                    <Route path='/login' element={<Formulaire title="Connexion"/>}/>
              </Routes>
            </div>
      </Router>
  );
}

export default App;
