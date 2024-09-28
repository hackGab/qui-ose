import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthProvider";

const PrivateRoute = ({ children, allowedRoles }) => {
    const { authState } = useAuth();
    const { isAuthenticated, role } = authState;

    console.log("Is Authenticated:", isAuthenticated, "Role:", role); // Debugging

    // Check if the user is authenticated and has one of the allowed roles
    return isAuthenticated && allowedRoles.includes(role) ? children : <Navigate to="/" />;
};

export default PrivateRoute;