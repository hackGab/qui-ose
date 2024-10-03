import React, { createContext, useContext, useState, useEffect } from "react";

// Create the AuthContext
const AuthContext = createContext();

// AuthProvider component
export const AuthProvider = () => {


};

// Custom hook to use the AuthContext
export const useAuth = () => useContext(AuthContext);
