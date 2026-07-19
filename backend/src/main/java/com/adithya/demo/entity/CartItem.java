package com.adithya.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cart cart;
    @ManyToOne
    private Product product;
    private Integer quantity;
}