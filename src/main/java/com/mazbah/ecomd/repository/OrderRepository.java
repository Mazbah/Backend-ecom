package com.mazbah.ecomd.repository;

import com.mazbah.ecomd.entity.Order;
import com.mazbah.ecomd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);
}
