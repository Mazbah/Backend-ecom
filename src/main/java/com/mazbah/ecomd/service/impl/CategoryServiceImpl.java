package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.entity.Category;
import com.mazbah.ecomd.repository.CategoryRepository;
import com.mazbah.ecomd.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("CategoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listCategories(){
        return categoryRepository.findAll();
    }

    public void createCategory(Category category){
        categoryRepository.save(category);
    }

    public Category readCategory(String categoryName){
        return categoryRepository.findByCategoryName(categoryName);
    }

    public Optional<Category> readCategory(Integer categoryId){
        return categoryRepository.findById(categoryId);
    }

    public void updateCategory(Integer categoryId, Category newCategory){
        Category category = categoryRepository.findById(categoryId).get();
        category.setCategoryName(newCategory.getCategoryName());
        category.setDescription(newCategory.getDescription());
        category.setProducts(newCategory.getProducts());
        category.setImageUrl(newCategory.getImageUrl());

        categoryRepository.save(newCategory);
    }
}
