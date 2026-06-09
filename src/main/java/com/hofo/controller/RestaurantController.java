package com.hofo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hofo.dto.RestaurantDTO;
import com.hofo.entity.Restaurant;
import com.hofo.repository.RestaurantRepository;
import com.hofo.service.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;
	private final RestaurantRepository restaurantRepository;

	public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurantRepository) {

		this.restaurantService = restaurantService;
		this.restaurantRepository = restaurantRepository;
	}

	@PostMapping
	public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {

		return restaurantService.saveRestaurant(restaurant);
	}

	@PutMapping
	public Restaurant updateRestaurant(@RequestBody Restaurant restaurant) {

		return restaurantService.updateRestaurant(restaurant);
	}

	@DeleteMapping("/{id}")
	public void deleteRestaurant(@PathVariable Integer id) {

		restaurantService.deleteRestaurant(id);
	}

	@GetMapping
	public List<Restaurant> getAllRestaurants() {

		return restaurantService.getAllRestaurants();
	}

	@GetMapping("/cities")
	public List<String> getCities() {

		return restaurantRepository.findDistinctCities();
	}

	@GetMapping("/city")
	public List<RestaurantDTO> getByCity(@RequestParam String city) {

		return restaurantService.findByCity(city);
	}

	@GetMapping("/nearby")
	public List<RestaurantDTO> nearbyRestaurants(@RequestParam double lat, @RequestParam double lng) {

		return restaurantService.findNearbyRestaurants(lat, lng);

	}
}