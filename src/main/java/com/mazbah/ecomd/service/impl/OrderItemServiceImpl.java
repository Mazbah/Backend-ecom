package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.entity.OrderItem;
import com.mazbah.ecomd.repository.OrderItemRepository;
import com.mazbah.ecomd.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("OrderItemService")
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void addOrderProducts(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }
}
