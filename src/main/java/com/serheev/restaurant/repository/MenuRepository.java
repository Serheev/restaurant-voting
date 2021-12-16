package com.serheev.restaurant.repository;

import com.serheev.restaurant.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepositoryImplementation<Menu, Integer> {
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.date = :date")
    Optional<Menu> findMenuByDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);
}
