	package com.hofo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hofo.entity.Restaurant;
import com.hofo.repository.RestaurantRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RestaurantRepository restaurantRepository;

    public AdminController(
            RestaurantRepository restaurantRepository) {

        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurant-page")
    public String restaurantPage() {
        return "admin-restaurant";
    }

    @ResponseBody
    @PostMapping("/restaurant")
    public Restaurant saveRestaurant(
			@RequestBody Restaurant restaurant) {

        return restaurantRepository.save(restaurant);
    }
}