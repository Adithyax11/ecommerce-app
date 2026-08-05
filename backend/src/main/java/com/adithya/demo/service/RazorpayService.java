package com.adithya.demo.service;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {
    @Value("${razorpay.key-id}")
    private String keyId;
    @Value("${razorpay.key-secret}")
    private String keySecret;

    public String createOrder(Double amount, Long orderId) throws Exception {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject options = new JSONObject();
        options.put("amount", (int) (amount * 100));
        options.put("currency", "INR");
        options.put("receipt", "order_" + orderId);
        return client.orders.create(options).get("id");
    }

    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws Exception {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_signature", razorpaySignature);
        return Utils.verifyPaymentSignature(options, keySecret);
    }

    public String getKeyId() {
        return keyId;
    }
}