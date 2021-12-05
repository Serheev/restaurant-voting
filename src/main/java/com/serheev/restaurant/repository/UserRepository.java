package com.serheev.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serheev.restaurant.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}