import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider'; // Access authState from context

const PrivateRoute = ({ children, allowedRoles }) => {
    const { authState } = useAuth();
    const { isAuthenticated, role } = authState; // Access role directly from authState

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (role && !allowedRoles.includes(role)) {
        return <Navigate to="/nonAutorise" replace />;
    }

    return children;
};

export default PrivateRoute;
