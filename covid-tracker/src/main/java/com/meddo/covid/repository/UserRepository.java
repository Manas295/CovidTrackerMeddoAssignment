package com.meddo.covid.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meddo.covid.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
}