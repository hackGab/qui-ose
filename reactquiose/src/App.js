import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import './App.css';
import Formulaire from "./components/Formulaire";

function App() {
  return (
      <Router>
          <div className='App-body'>
              <Routes>
                    <Route path='/' element={<Navigate to='/signUp' />}/>
                    <Route path='/signUp' element={<Formulaire title="Inscription"/>}/>
                    <Route path='/login' element={<Formulaire title="Connexion"/>}/>
              </Routes>
            </div>
      </Router>
  );
}

export default App;
