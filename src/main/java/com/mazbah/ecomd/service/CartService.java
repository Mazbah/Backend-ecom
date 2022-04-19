package com.mazbah.ecomd.service;

import com.mazbah.ecomd.dto.cart.AddToCartDto;
import com.mazbah.ecomd.dto.cart.CartDto;
import com.mazbah.ecomd.entity.Product;
import com.mazbah.ecomd.entity.User;

public interface CartService {
    public void addTOCart(AddToCartDto addToCartDto, Product product, User user);
    public CartDto listCartItems(User user);
    public void updateCartItem(AddToCartDto cartDto, User user, Product product);
    public void deleteCartItem(int id, int userId);
    public void deleteCartItems(int userId);
    public void deleteUserCartItems(User user);
}
