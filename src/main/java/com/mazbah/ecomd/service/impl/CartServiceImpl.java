package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.dto.cart.AddToCartDto;
import com.mazbah.ecomd.dto.cart.CartDto;
import com.mazbah.ecomd.dto.cart.CartItemDto;
import com.mazbah.ecomd.entity.Cart;
import com.mazbah.ecomd.entity.Product;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.CartItemNotExistException;
import com.mazbah.ecomd.repository.CartRepository;
import com.mazbah.ecomd.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private  CartRepository cartRepository;

    public CartServiceImpl(){}

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void addTOCart(AddToCartDto addToCartDto, Product product, User user) {
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }

    @Override
    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        for(Cart cart: cartList){
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }
        double totalCost = 0;

        for(CartItemDto cartItemDto: cartItems){
            totalCost += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }
        return new CartDto(cartItems, totalCost);
    }

    public static CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }

    public void updateCartItem(AddToCartDto cartDto, User user, Product product){
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreateDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(int id, int userId) throws CartItemNotExistException{
        if(!cartRepository.existsById(id))
            throw new CartItemNotExistException("Cart id is invalid : "+id);
        cartRepository.deleteById(id);
    }

    public void deleteCartItems(int userId) { cartRepository.deleteAll(); }

    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

}
