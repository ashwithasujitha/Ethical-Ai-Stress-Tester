package com.example.demo.Service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.demo.Entity.Restaurant;
import com.example.demo.Repository.RestaurantRepository;

public class RestauarantService {
        @Autowired
    RestaurantRepository restaurantRepo;

    public Restaurant createRestaurant(Restaurant r) {
        return restaurantRepo.save(r);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }
    public List<Restaurant> getByname(String name) {
        return restaurantRepo.findByName(name);
    }
    public Optional<Restaurant> getById(long id) {
        return restaurantRepo.findById(id);
    }

    public Restaurant updateRestaurant(Long id, Restaurant r) {
        r.setId(id);
        return restaurantRepo.save(r);
    }

    public String deleteById(long id) {
        restaurantRepo.deleteById(id);
        return "Success";
    }

    public Page<Restaurant> getRestaurantByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return restaurantRepo.findAll(pageable);
    }

    public List<Restaurant> sortByName() {
        return restaurantRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Restaurant> getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        return restaurantRepo.findByName(name);
    }

}
