package com.mazbah.ecomd.service;

import com.mazbah.ecomd.entity.WishList;

import java.util.List;

public interface WishListService {
    public void createWishList(WishList wishList);
    public List<WishList> readWishList(Integer userId);
}
