import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const STEPS = ["PLACED", "CONFIRMED", "PREPARING", "READY_FOR_PICKUP", "COMPLETED"];

function OrderTracking() {
    const { id } = useParams();
    const [order, setOrder] = useState(null);

    useEffect(() => {
        api.get(`/orders/${id}`).then((res) => setOrder(res.data));

        const socket = new SockJS("http://localhost:8080/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                client.subscribe(`/topic/orders/${id}`, (message) => {
                    const newStatus = JSON.parse(message.body);
                    setOrder((prev) => ({ ...prev, status: newStatus }));
                });
            },
        });
        client.activate();

        return () => client.deactivate();
    }, [id]);

    const handlePay = async () => {
        const { data } = await api.post(`/payments/create-order/${id}`);
        const options = {
            key: data.keyId,
            amount: order.totalAmount * 100,
            currency: "INR",
            name: "Foodcourt",
            order_id: data.razorpayOrderId,
            handler: async (response) => {
                await api.post("/payments/verify", {
                    orderId: order.id,
                    razorpayOrderId: response.razorpay_order_id,
                    razorpayPaymentId: response.razorpay_payment_id,
                    razorpaySignature: response.razorpay_signature,
                });
            },
        };
        const rzp = new window.Razorpay(options);
        rzp.open();
    };

    if (!order) return <div className="p-6">Loading...</div>;

    const currentStep = STEPS.indexOf(order.status);

    return (
        <div className="p-6 max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold mb-6">Order #{order.id}</h2>

            <div className="flex justify-between mb-8">
                {STEPS.map((step, i) => (
                    <div key={step} className="flex-1 text-center">
                        <div className={`w-8 h-8 mx-auto rounded-full flex items-center justify-center text-white ${i <= currentStep ? "bg-orange-600" : "bg-gray-300"}`}>
                            {i + 1}
                        </div>
                        <p className="text-xs mt-1">{step.replace(/_/g, " ")}</p>
                    </div>
                ))}
            </div>

            <div className="space-y-3 mb-6">
                {order.items.map((item) => (
                    <div key={item.id} className="flex justify-between border-b pb-2">
                        <span>{item.product.name} x{item.quantity}</span>
                        <span>₹{item.price * item.quantity}</span>
                    </div>
                ))}
            </div>

            <p className="text-xl font-bold mb-6">Total: ₹{order.totalAmount}</p>

            {order.status === "PLACED" && (
                <button onClick={handlePay} className="bg-orange-600 text-white px-6 py-3 rounded-lg">
                    Pay Now
                </button>
            )}
        </div>
    );
}

export default OrderTracking;