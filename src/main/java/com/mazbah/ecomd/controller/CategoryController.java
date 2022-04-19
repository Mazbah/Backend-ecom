package com.mazbah.ecomd.controller;

import com.mazbah.ecomd.common.ApiResponse;
import com.mazbah.ecomd.entity.Category;
import com.mazbah.ecomd.service.CategoryService;
import com.mazbah.ecomd.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> body = categoryService.listCategories();
        return new ResponseEntity<List<Category>>(body,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody Category category){
        if(Helper.notNull(categoryService.readCategory(category.getCategoryName()))){
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Category Already Exixts!!!"), HttpStatus.CONFLICT);
        }
        categoryService.createCategory(category);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "New Category Created"), HttpStatus.CREATED);
    }

    @PostMapping("/update/{categoryID}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryID")Integer categoryID, @Valid @RequestBody Category category){
        // Check to see if the category exists.
        if(Helper.notNull(categoryService.readCategory(categoryID))){
            // If the category exists then update it.
            categoryService.updateCategory(categoryID, category);
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(true, "updated the category"), HttpStatus.OK
            );
        }
        // If the category doesn't exist then return a response of unsuccessful.
        return new ResponseEntity<ApiResponse>(
                new ApiResponse(false, "Category doesn't exists!!!"), HttpStatus.NOT_FOUND
        );
    }
}
