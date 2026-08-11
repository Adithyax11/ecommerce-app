import { createContext, useContext, useState } from "react";
import api from "../api/axios";

const CartContext = createContext();

export function CartProvider({ children }) {
    const [cart, setCart] = useState(null);

    const refreshCart = async () => {
        const res = await api.get("/cart");
        setCart(res.data);
    };

    const addToCart = async (productId, quantity) => {
        const res = await api.post("/cart/add", { productId, quantity });
        setCart(res.data);
    };

    const updateItem = async (itemId, quantity) => {
        const res = await api.put(`/cart/update/${itemId}`, { quantity });
        setCart(res.data);
    };

    const removeItem = async (itemId) => {
        const res = await api.delete(`/cart/remove/${itemId}`);
        setCart(res.data);
    };

    const placeOrder = async () => {
        const res = await api.post("/orders/place");
        setCart(null);
        return res.data;
    };

    return (
        <CartContext.Provider value={{ cart, refreshCart, addToCart, updateItem, removeItem, placeOrder }}>
            {children}
        </CartContext.Provider>
    );
}

export function useCart() {
    return useContext(CartContext);
}