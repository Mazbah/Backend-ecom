package com.mazbah.ecomd.repository;

import com.mazbah.ecomd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    User findByEmail(String email);
    User findUserByEmail(String email);
}
