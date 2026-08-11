import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import { useCart } from "../context/CartContext";

function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const { addToCart } = useCart();

    useEffect(() => {
        api.get(`/products/${id}`).then((res) => setProduct(res.data));
    }, [id]);

    if (!product) return <div className="p-6">Loading...</div>;

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h2 className="text-2xl font-bold">{product.name}</h2>
            <p className="text-gray-500 mt-2">{product.description}</p>
            <p className="text-orange-600 font-bold text-xl mt-4">₹{product.price}</p>
            <div className="flex items-center gap-4 mt-6">
                <input
                    type="number"
                    min="1"
                    value={quantity}
                    onChange={(e) => setQuantity(Number(e.target.value))}
                    className="border p-2 w-20 rounded"
                />
                <button
                    onClick={() => addToCart(product.id, quantity)}
                    className="bg-orange-600 text-white px-6 py-2 rounded-lg"
                >
                    Add to Cart
                </button>
            </div>
        </div>
    );
}

export default ProductDetail;