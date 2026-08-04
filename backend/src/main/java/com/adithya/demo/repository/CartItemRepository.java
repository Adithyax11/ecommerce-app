package com.adithya.demo.repository;

import com.adithya.demo.entity.Cart;
import com.adithya.demo.entity.CartItem;
import com.adithya.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}