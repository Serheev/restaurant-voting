package com.serheev.restaurant.repository;

import com.serheev.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepositoryImplementation<Restaurant, Integer> {
    List<Restaurant> findAllByNameIsContainingIgnoreCase(String name);
}
