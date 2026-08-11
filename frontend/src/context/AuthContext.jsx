import { createContext, useContext, useState } from "react";
import api from "../api/axios";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [username, setUsername] = useState(localStorage.getItem("username"));

    const login = async (username, password) => {
        const res = await api.post("/auth/login", { username, password });
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("username", username);
        setUsername(username);
    };

    const register = async (username, email, password) => {
        const res = await api.post("/auth/register", { username, email, password });
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("username", username);
        setUsername(username);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        setUsername(null);
    };

    return (
        <AuthContext.Provider value={{ username, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}