package com.wsapoa.services;

import com.wsapoa.dto.ProductRequestDTO;
import com.wsapoa.entity.Products;
import com.wsapoa.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;

    public void createProduct(ProductRequestDTO productRequestDTO) {
        Products product = new Products(productRequestDTO);
        productsRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Products product = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productRequestDTO.getName());
        product.setWidth(productRequestDTO.getWidth());
        product.setHeight(productRequestDTO.getHeight());
        product.setLength(productRequestDTO.getLength());
        product.setUsed(productRequestDTO.isUsed());
        product.setProductVolume(productRequestDTO.getProductVolume());
        productsRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }

    public Products readProduct(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }
}