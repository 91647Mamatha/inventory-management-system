package com.example.inventory.service;

import com.example.inventory.entity.CartItem;
import com.example.inventory.entity.Customer;
import com.example.inventory.entity.OrderEntity;
import com.example.inventory.entity.OrderItem;
import com.example.inventory.entity.Product;
import com.example.inventory.repository.OrderRepository;
import com.example.inventory.repository.OrderItemRepository;
import com.example.inventory.repository.CustomerRepository;
import com.example.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void saveOrder(String customerName, String address, String phone, List<CartItem> cart) {

        // 1) Save Customer
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer = customerRepository.save(customer);

        // 2) Calculate total
        BigDecimal total = cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3) Save Order
        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setStatus("PLACED");

        order = orderRepository.save(order);

        // 4) Save OrderItems + UPDATE STOCK
        for (CartItem c : cart) {

            Product product = productRepository.findById(c.getProductId())
                    .orElseThrow();

            // 🔥 STOCK UPDATE (FINAL FIX)
            product.setQuantity(product.getQuantity() - c.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(c.getQuantity());
            item.setSubtotal(c.getSubtotal());

            orderItemRepository.save(item);
        }
    }
}