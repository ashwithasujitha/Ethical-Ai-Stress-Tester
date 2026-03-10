package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Orders;
import com.example.demo.Service.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    
    @Autowired
    OrdersService ordersService;

    @PostMapping("/post")
    public Orders insertOrder(@RequestBody Orders order) {
        return ordersService.saveOrder(order);
    }

    @GetMapping("/get")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrderEntities();
    }

    @GetMapping("/{id}")
    public Optional<Orders> getOrderById(@PathVariable Long id) {
        return ordersService.getByOrderId(id);
    }

    @PutMapping("/{id}")
    public Orders updateOrder(@PathVariable Long id, @RequestBody Orders order) {
        return ordersService.UpdateOrderE(id, order);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return ordersService.DeleteById(id);
    }

    @GetMapping("/page")
    public Page<Orders> getOrdersByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return ordersService.getOrderByPage(page, size);
    }

    @GetMapping("/sort")
    public List<Orders> sortOrders() {
        return ordersService.sortOrder();
    }

    @GetMapping("/query/{id}")
    public Optional<Orders> getOrderByQuery(@PathVariable Long id) {
        return ordersService.getByQuery(id);
    }
}
