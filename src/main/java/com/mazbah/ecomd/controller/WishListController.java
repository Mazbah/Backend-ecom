package com.mazbah.ecomd.controller;

import com.mazbah.ecomd.common.ApiResponse;
import com.mazbah.ecomd.dto.product.ProductDto;
import com.mazbah.ecomd.entity.Product;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.entity.WishList;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.service.WishListService;
import com.mazbah.ecomd.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired WishListService wishListService;
    @Autowired AuthenticationService authenticationService;

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getAllWishlists(@PathVariable("token") String token){
        int user_id = authenticationService.getUser(token).getId();
        List<WishList> body = wishListService.readWishList(user_id);
        List<ProductDto> products = new ArrayList<>();
        for (WishList wishList: body){
            products.add(ProductServiceImpl.getDtoFromProduct(wishList.getProduct()));
        }
        return new ResponseEntity<List<ProductDto>>(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody Product product, @RequestParam("token") String token){
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        WishList wishList = new WishList(user, product);
        wishListService.createWishList(wishList);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Add to wishlist"), HttpStatus.CREATED);
    }
}
