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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Restaurant;
import com.example.demo.Service.RestauarantService;


@RestController
@RequestMapping("/userapi")
public class RestaurantController {
        @Autowired
    private RestauarantService restaurantService;

    @PostMapping("/rpost")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.createRestaurant(restaurant);
    }

    @GetMapping("/rget")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/r/{id}")
    public Optional<Restaurant> getRestaurantById(@PathVariable Long id) {
        return restaurantService.getById(id);
    }

    @DeleteMapping("/rc/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        return restaurantService.deleteById(id);
    }

    @PutMapping("/rput/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurant(id, restaurant);
    }

    @GetMapping("/page")
    public Page<Restaurant> getRestaurantsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return restaurantService.getRestaurantByPage(page, size);
    }

    @GetMapping("/sort")
    public List<Restaurant> sortByRestaurantName() {
        return restaurantService.sortByName();
    }

    @GetMapping("/searchR/{name}")
    public List<Restaurant> searchRestaurantByName(@PathVariable String name) {
        return restaurantService.getByName(name);
    }

}
