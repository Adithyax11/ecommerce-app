import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Register() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { register } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await register(username, email, password);
            navigate("/products");
        } catch {
            setError("Registration failed — username or email may already be taken");
        }
    };

    return (
        <div className="p-6 max-w-sm mx-auto">
            <h2 className="text-2xl font-bold mb-6">Register</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <input type="text" placeholder="Username" value={username}
                       onChange={(e) => setUsername(e.target.value)} className="border p-2 w-full rounded" />
                <input type="email" placeholder="Email" value={email}
                       onChange={(e) => setEmail(e.target.value)} className="border p-2 w-full rounded" />
                <input type="password" placeholder="Password" value={password}
                       onChange={(e) => setPassword(e.target.value)} className="border p-2 w-full rounded" />
                {error && <p className="text-red-500 text-sm">{error}</p>}
                <button type="submit" className="bg-orange-600 text-white w-full py-2 rounded-lg">
                    Register
                </button>
            </form>
            <p className="text-sm mt-4">
                Already have an account? <Link to="/login" className="text-orange-600">Login</Link>
            </p>
        </div>
    );
}

export default Register;