package com.mazbah.ecomd.service;

import com.mazbah.ecomd.dto.checkout.CheckOutItemDto;
import com.mazbah.ecomd.entity.Order;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.OrderNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface OrderService {
    public Session createSession(List<CheckOutItemDto> checkOutItemDtoList) throws StripeException;
    public void placeOrder(User user, String sessionId);
    public List<Order> listOrders(User user);
    public Order getOrder(Integer orderId) throws OrderNotFoundException;
}
