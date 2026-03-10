package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.OrderItem;
import com.example.demo.Repository.OrderItemRepository;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository orderrepo;

    public OrderItem createOrder(OrderItem o) {
        return orderrepo.save(o);
    }

    public List<OrderItem> getAllorder() {
        return orderrepo.findAll();
    }

    public Optional<OrderItem> getById(Long id) {
        return orderrepo.findById(id);
    }

    public OrderItem updateOrder(Long id, OrderItem o) {
        o.setId(id);
        return orderrepo.save(o);
    }

    public String deleteById(long id) {
        orderrepo.deleteById(id);
        return "Success";
    }

    public Page<OrderItem> getOrdersByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return orderrepo.findAll(pageable);
    }

    public List<OrderItem> sortByOrderDate() {  // Sorting by orderDate instead of username
        return orderrepo.findAll(Sort.by(Sort.Direction.ASC, "orderDate"));
    }

    public List<OrderItem> getByOrderId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return orderrepo.findById(id).stream().toList();
    }

}
