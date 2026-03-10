package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.OrderItem;
import com.example.demo.Service.OrderItemService;

public class OrderItemController {
    
    @Autowired
    OrderItemService orderServ;

    @PostMapping("/postO")
    public OrderItem insertOrder(@RequestBody OrderItem o) {
        return orderServ.createOrder(o);
    }

    @GetMapping("/getO")
    public List<OrderItem> getAllOrders() {
        return orderServ.getAllorder();
    }

    @GetMapping("/Order/{id}")
    public Optional<OrderItem> getOrderById(@PathVariable Long id) {
        return orderServ.getById(id);
    }

    @DeleteMapping("/deleteO/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderServ.deleteById(id);
    }

    @PutMapping("/Order/{id}")
    public OrderItem updateOrder(@PathVariable Long id, @RequestBody OrderItem o) {
        return orderServ.updateOrder(id, o);
    }

    @GetMapping("/page")
    public Page<OrderItem> getByPage(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        return orderServ.getOrdersByPage(page, size);
    }

    @GetMapping("/sort")
    public List<OrderItem> sortByOrderDate() {  // Sorting by order date instead of username
        return orderServ.sortByOrderDate();
    }

    @GetMapping("/orderById/{id}")
    public List<OrderItem> getByOrderId(@PathVariable Long id) {
        return orderServ.getByOrderId(id);
    }

}
