package com.example.inventory.repository;

import com.example.inventory.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    long count();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o")
    BigDecimal getTotalRevenue();
}
