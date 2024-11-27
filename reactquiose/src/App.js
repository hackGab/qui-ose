import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { routes } from "./utils/routes";
import "./App.css"

function App() {
    return (
        <Router>
            <div className="App-body">
                <Routes>
                    {routes.map((route, index) => (
                        <Route key={index} path={route.path} element={route.element} />
                    ))}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
