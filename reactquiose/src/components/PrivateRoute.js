import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider'; // Access authState from context

const PrivateRoute = ({ children, allowedRoles }) => {
    const { authState } = useAuth();
    const { isAuthenticated, userData } = authState;

    if (!isAuthenticated || !userData) {
        return <Navigate to="/login" replace />;
    }

    if (!allowedRoles.includes(userData.role)) {
        return <Navigate to="/nonAutorise" replace />;
    }

    return children;
};

export default PrivateRoute;
