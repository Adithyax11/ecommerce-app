package com.adithya.demo.dto;
import com.adithya.demo.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}