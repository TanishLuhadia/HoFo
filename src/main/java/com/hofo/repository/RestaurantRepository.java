package com.hofo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hofo.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	List<Restaurant> findByCityIgnoreCase(String city);

	List<Restaurant> findByActiveTrue();

	@Query("""
			select distinct r.city
			from Restaurant r
			where r.active=true
			order by r.city
			""")
	List<String> findDistinctCities();
}