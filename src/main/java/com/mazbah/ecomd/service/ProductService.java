package com.mazbah.ecomd.service;

import com.mazbah.ecomd.dto.product.ProductDto;
import com.mazbah.ecomd.entity.Category;
import com.mazbah.ecomd.entity.Product;

import java.util.List;

public interface ProductService {
    public List<ProductDto> listProducts();
    public void addProduct(ProductDto productDto, Category category);
    public void updateProduct(Integer productID, ProductDto productDto, Category category);
    public Product getProductById(Integer productID);
}
