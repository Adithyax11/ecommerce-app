import { Link, useNavigate } from "react-router-dom";
import { ShoppingCart } from "lucide-react";
import { useAuth } from "../context/AuthContext";

function Navbar() {
    const { username, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <nav className="flex items-center justify-between px-6 py-4 bg-white shadow">
            <Link to="/" className="text-xl font-bold text-orange-600">Foodcourt</Link>
            <div className="flex items-center gap-6">
                <Link to="/products" className="text-gray-700 hover:text-orange-600">Menu</Link>
                <Link to="/cart" className="text-gray-700 hover:text-orange-600">
                    <ShoppingCart size={22} />
                </Link>
                {username ? (
                    <>
                        <span className="text-gray-600 text-sm">Hi, {username}</span>
                        <button onClick={handleLogout} className="text-gray-700 hover:text-orange-600">Logout</button>
                    </>
                ) : (
                    <Link to="/login" className="text-gray-700 hover:text-orange-600">Login</Link>
                )}
            </div>
        </nav>
    );
}

export default Navbar;