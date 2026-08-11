import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { Trash2 } from "lucide-react";

function Cart() {
    const { cart, refreshCart, updateItem, removeItem, placeOrder } = useCart();
    const navigate = useNavigate();

    useEffect(() => {
        refreshCart();
    }, []);

    if (!cart) return <div className="p-6">Loading...</div>;

    const items = cart.items || [];
    const total = items.reduce((sum, i) => sum + i.price * i.quantity, 0);

    const handlePlaceOrder = async () => {
        const order = await placeOrder();
        navigate(`/orders/${order.id}`);
    };

    return (
        <div className="p-6 max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold mb-6">Your Cart</h2>
            {items.length === 0 ? (
                <p className="text-gray-500">Your cart is empty.</p>
            ) : (
                <>
                    <div className="space-y-4">
                        {items.map((item) => (
                            <div key={item.id} className="flex items-center justify-between border rounded-lg p-4">
                                <div>
                                    <h3 className="font-semibold">{item.product.name}</h3>
                                    <p className="text-orange-600">₹{item.price}</p>
                                </div>
                                <div className="flex items-center gap-3">
                                    <input
                                        type="number"
                                        min="1"
                                        value={item.quantity}
                                        onChange={(e) => updateItem(item.id, Number(e.target.value))}
                                        className="border p-1 w-16 rounded text-center"
                                    />
                                    <button onClick={() => removeItem(item.id)} className="text-red-500">
                                        <Trash2 size={20} />
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 flex items-center justify-between">
                        <p className="text-xl font-bold">Total: ₹{total}</p>
                        <button onClick={handlePlaceOrder} className="bg-orange-600 text-white px-6 py-3 rounded-lg">
                            Place Order
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default Cart;