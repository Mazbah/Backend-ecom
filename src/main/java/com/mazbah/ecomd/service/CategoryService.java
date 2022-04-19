package com.mazbah.ecomd.service;

import com.mazbah.ecomd.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<Category> listCategories();
    public void createCategory(Category category);
    public Category readCategory(String categoryName);
    public Optional<Category> readCategory(Integer categoryId);
    public void updateCategory(Integer categoryId, Category newCategory);
}
