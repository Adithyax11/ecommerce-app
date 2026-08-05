package com.adithya.demo.controller;

import com.adithya.demo.dto.PaymentVerificationRequest;
import com.adithya.demo.entity.Order;
import com.adithya.demo.enums.OrderStatus;
import com.adithya.demo.repository.OrderRepository;
import com.adithya.demo.service.RazorpayService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PaymentController(RazorpayService razorpayService, OrderRepository orderRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.razorpayService = razorpayService;
        this.orderRepository = orderRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/create-order/{orderId}")
    public Map<String, String> createOrder(@PathVariable Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));
        String razorpayOrderId = razorpayService.createOrder(order.getTotalAmount(), orderId);
        return Map.of("razorpayOrderId", razorpayOrderId, "keyId", razorpayService.getKeyId());
    }

    @PostMapping("/verify")
    public Order verify(@RequestBody PaymentVerificationRequest req) throws Exception {
        boolean valid = razorpayService.verifySignature(req.getRazorpayOrderId(), req.getRazorpayPaymentId(), req.getRazorpaySignature());
        if (!valid) throw new RuntimeException("payment verification failed");

        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("order not found"));
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), order.getStatus());
        return order;
    }
}