package com.mazbah.ecomd.repository;

import com.mazbah.ecomd.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
