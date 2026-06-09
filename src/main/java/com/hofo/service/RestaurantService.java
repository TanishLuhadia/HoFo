package com.hofo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hofo.dto.RestaurantDTO;
import com.hofo.entity.Restaurant;
import com.hofo.repository.RestaurantRepository;

@Service
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	public RestaurantService(RestaurantRepository restaurantRepository) {

		this.restaurantRepository = restaurantRepository;
	}

	public Restaurant saveRestaurant(Restaurant restaurant) {

		return restaurantRepository.save(restaurant);
	}

	public List<Restaurant> getAllRestaurants() {

		return restaurantRepository.findAll();
	}

	public void deleteRestaurant(Integer id) {

		restaurantRepository.deleteById(id);
	}

	public Restaurant updateRestaurant(Restaurant restaurant) {

		return restaurantRepository.save(restaurant);
	}

	public List<RestaurantDTO> findNearbyRestaurants(double userLat, double userLng) {

		List<Restaurant> restaurants = restaurantRepository.findAll();

		List<RestaurantDTO> result = new ArrayList<>();

		for (Restaurant restaurant : restaurants) {

			RestaurantDTO dto = new RestaurantDTO();

			dto.setId(restaurant.getId());
			dto.setName(restaurant.getName());
			dto.setDescription(restaurant.getDescription());
			dto.setCity(restaurant.getCity());
			dto.setAddress(restaurant.getAddress());
			dto.setImageUrl(restaurant.getImageUrl());

			double distance = calculateDistance(userLat, userLng, restaurant.getLatitude(), restaurant.getLongitude());

			dto.setDistance(distance);

			result.add(dto);
		}

		result.sort(Comparator.comparingDouble(RestaurantDTO::getDistance));

		return result;
	}

	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

		final int R = 6371;

		double latDistance = Math.toRadians(lat2 - lat1);

		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)

				+

				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2)
						* Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c;
	}
	
	public List<RestaurantDTO> findByCity(String city) {

	    List<Restaurant> restaurants =
	            restaurantRepository.findByCityIgnoreCase(city);

	    List<RestaurantDTO> result =
	            new ArrayList<>();

	    for(Restaurant restaurant : restaurants) {

	        RestaurantDTO dto =
	                new RestaurantDTO();

	        dto.setId(restaurant.getId());
	        dto.setName(restaurant.getName());
	        dto.setDescription(restaurant.getDescription());
	        dto.setCity(restaurant.getCity());
	        dto.setAddress(restaurant.getAddress());
	        dto.setImageUrl(restaurant.getImageUrl());

	        result.add(dto);
	    }

	    return result;
	}
}