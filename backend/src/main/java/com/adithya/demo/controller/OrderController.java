package com.adithya.demo.controller;

import com.adithya.demo.dto.UpdateOrderStatusRequest;
import com.adithya.demo.entity.*;
import com.adithya.demo.enums.OrderStatus;
import com.adithya.demo.enums.Role;
import com.adithya.demo.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderController(OrderRepository orderRepository, CartRepository cartRepository,
                           CartItemRepository cartItemRepository, UserRepository userRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private User getCurrentUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    @PostMapping("/place")
    public Order placeOrder(Authentication auth) {
        User user = getCurrentUser(auth);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("cart is empty"));
        if (cart.getItems() == null || cart.getItems().isEmpty())
            throw new RuntimeException("cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
            orderItems.add(oi);
            total += ci.getProduct().getPrice() * ci.getQuantity();
        }
        order.setItems(orderItems);
        order.setTotalAmount(total);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

        return order;
    }

    @GetMapping
    public List<Order> getMyOrders(Authentication auth) {
        return orderRepository.findByUser(getCurrentUser(auth));
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id, Authentication auth) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("order not found"));
        User user = getCurrentUser(auth);
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN)
            throw new RuntimeException("not authorized to view this order");
        return order;
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("order not found"));
        order.setStatus(req.getStatus());
        orderRepository.save(order);
        messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), order.getStatus());
        return order;
    }
}