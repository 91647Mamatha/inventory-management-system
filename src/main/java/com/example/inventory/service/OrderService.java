package com.example.inventory.service;

import com.example.inventory.entity.CartItem;

import java.util.List;

public interface OrderService {
    void saveOrder(String customerName, String address, String phone, List<CartItem> cartItems);
}
