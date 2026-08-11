import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(username, password);
            navigate("/products");
        } catch {
            setError("Invalid username or password");
        }
    };

    return (
        <div className="p-6 max-w-sm mx-auto">
            <h2 className="text-2xl font-bold mb-6">Login</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <input type="text" placeholder="Username" value={username}
                       onChange={(e) => setUsername(e.target.value)} className="border p-2 w-full rounded" />
                <input type="password" placeholder="Password" value={password}
                       onChange={(e) => setPassword(e.target.value)} className="border p-2 w-full rounded" />
                {error && <p className="text-red-500 text-sm">{error}</p>}
                <button type="submit" className="bg-orange-600 text-white w-full py-2 rounded-lg">
                    Login
                </button>
            </form>
            <p className="text-sm mt-4">
                No account? <Link to="/register" className="text-orange-600">Register</Link>
            </p>
        </div>
    );
}

export default Login;