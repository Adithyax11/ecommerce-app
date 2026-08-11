import { Link } from "react-router-dom";

function Home() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-orange-50">
            <h1 className="text-4xl font-bold text-orange-600">Foodcourt</h1>
            <p className="text-gray-600 mt-2">Order from your favorite stalls</p>
            <Link to="/products" className="mt-6 px-6 py-3 bg-orange-600 text-white rounded-lg">
                Browse Menu
            </Link>
        </div>
    );
}

export default Home;