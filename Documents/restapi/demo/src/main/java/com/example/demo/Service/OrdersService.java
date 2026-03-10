package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Orders;
import com.example.demo.Repository.OrderR;

@Service
public class OrdersService {
         @Autowired
    OrderR Or;

    public Orders saveOrder(Orders order) {
        return Or.save(order);
    }
    public List<Orders>getAllOrderEntities()
    {
        return Or.findAll();
    }
   
    public Orders UpdateOrderE(Long id,Orders e)
    {
        e.setId(id);
        return Or.save(e);
    }
    public String DeleteById(long id)
    {
        Or.deleteById(id);
        return "Success";
    }
    
    public Page<Orders> getOrderByPage(int page,int size)
    {
        PageRequest pageable=PageRequest.of(page, size);
        return Or.findAll(pageable);
    }
    public List<Orders>sortOrder()
    {
        return Or.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    public Optional<Orders> getByQuery(Long id)
    {
        return Or.findById(id);
    }
    public Optional<Orders> getByOrderId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }
        return Or.findById(id);
                
    }

}
