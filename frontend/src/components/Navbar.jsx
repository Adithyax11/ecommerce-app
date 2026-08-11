import { Link } from "react-router-dom";
import { ShoppingCart } from "lucide-react";

function Navbar() {
    return (
        <nav className="flex items-center justify-between px-6 py-4 bg-white shadow">
            <Link to="/" className="text-xl font-bold text-orange-600">Foodcourt</Link>
            <div className="flex items-center gap-6">
                <Link to="/products" className="text-gray-700 hover:text-orange-600">Menu</Link>
                <Link to="/cart" className="text-gray-700 hover:text-orange-600">
                    <ShoppingCart size={22} />
                </Link>
            </div>
        </nav>
    );
}

export default Navbar;