package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.dto.product.ProductDto;
import com.mazbah.ecomd.entity.Category;
import com.mazbah.ecomd.entity.Product;
import com.mazbah.ecomd.exceptions.ProductNotExistException;
import com.mazbah.ecomd.repository.ProductRepository;
import com.mazbah.ecomd.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ProductService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<ProductDto> listProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: products){
            ProductDto productDto = getDtoFromProduct(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public static ProductDto getDtoFromProduct(Product product){
        ProductDto productDto = new ProductDto(product);
        return productDto;
    }

    public static Product getProductFromDto(ProductDto productDto, Category category){
        Product product = new Product(productDto, category);
        return product;
    }

    public void addProduct(ProductDto productDto, Category category){
        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public void updateProduct(Integer productID, ProductDto productDto, Category category){
        Product product = getProductFromDto(productDto, category);
        product.setId(productID);
        productRepository.save(product);
    }

    public Product getProductById(Integer productID) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(productID);
        if(!optionalProduct.isPresent()){
            throw new ProductNotExistException("Product ID is invalid " + productID);
        }
        return optionalProduct.get();
    }

}
