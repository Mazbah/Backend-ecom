package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.entity.WishList;
import com.mazbah.ecomd.repository.WishListRepository;
import com.mazbah.ecomd.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WishListService")
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void createWishList(WishList wishList){
        wishListRepository.save(wishList);
    }

    public List<WishList> readWishList(Integer userId){
        return wishListRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }
}
