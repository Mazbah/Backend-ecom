package com.mazbah.ecomd.controller;

import com.mazbah.ecomd.common.ApiResponse;
import com.mazbah.ecomd.dto.cart.AddToCartDto;
import com.mazbah.ecomd.dto.cart.CartDto;
import com.mazbah.ecomd.entity.Product;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.exceptions.CartItemNotExistException;
import com.mazbah.ecomd.exceptions.ProductNotExistException;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.service.CartService;
import com.mazbah.ecomd.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/category")
public class CartController {
    @Autowired CartService cartService;

    @Autowired ProductService productService;

    @Autowired AuthenticationService authenticationService;

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCarts(
        @RequestBody AddToCartDto addToCartDto,
        @RequestParam("token") String token
    ) throws AuthenticationFailException, ProductNotExistException{

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Product product = productService.getProductById(addToCartDto.getProductId());
        System.out.println("product to add" + product.getName());
        cartService.addTOCart(addToCartDto, product, user);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true,"Added to cart sucessfully.."), HttpStatus.CREATED);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCarts(
         @RequestBody AddToCartDto cartDto,
         @RequestParam("token") String token
    ) throws AuthenticationFailException, ProductNotExistException{

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Product product = productService.getProductById(cartDto.getProductId());
        cartService.updateCartItem(cartDto, user, product);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCart(
            @PathVariable("cartItemId") int itemID,
            @RequestParam("token") String token
    ) throws AuthenticationFailException, CartItemNotExistException{

        authenticationService.authenticate(token);
        int userId = authenticationService.getUser(token).getId();
        cartService.deleteCartItem(itemID, userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been deleted"), HttpStatus.OK);
    }
}
