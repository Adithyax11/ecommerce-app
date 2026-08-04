package com.adithya.demo.controller;

import com.adithya.demo.dto.AddToCartRequest;
import com.adithya.demo.dto.UpdateCartItemRequest;
import com.adithya.demo.entity.*;
import com.adithya.demo.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartController(CartRepository cartRepository, CartItemRepository cartItemRepository,
                          ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            return cartRepository.save(cart);
        });
    }

    @GetMapping
    public Cart getCart(Authentication auth) {
        return getOrCreateCart(getCurrentUser(auth));
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody AddToCartRequest req, Authentication auth) {
        Cart cart = getOrCreateCart(getCurrentUser(auth));
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });
        item.setQuantity(item.getQuantity() + req.getQuantity());
        cartItemRepository.save(item);
        return cartRepository.findById(cart.getId()).get();
    }

    @PutMapping("/update/{itemId}")
    public Cart updateItem(@PathVariable Long itemId, @RequestBody UpdateCartItemRequest req, Authentication auth) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("cart item not found"));
        item.setQuantity(req.getQuantity());
        cartItemRepository.save(item);
        return cartRepository.findById(item.getCart().getId()).get();
    }

    @DeleteMapping("/remove/{itemId}")
    public Cart removeItem(@PathVariable Long itemId, Authentication auth) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("cart item not found"));
        Long cartId = item.getCart().getId();
        cartItemRepository.deleteById(itemId);
        return cartRepository.findById(cartId).get();
    }
}