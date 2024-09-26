package com.wsapoa.services;

import com.wsapoa.dto.ProductRequestDTO;
import com.wsapoa.entity.Product;
import com.wsapoa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements BaseService<Product, Long, ProductRequestDTO> {
    @Override
    public void create(ProductRequestDTO productRequestDTO) {

    }

    @Override
    public void update(Long aLong, ProductRequestDTO productRequestDTO) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Product read(Long aLong) {
        return null;
    }

    @Override
    public List<Product> getAll() {
        return List.of();
    }

    private final ProductRepository productRepository;

    public void createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product(productRequestDTO);
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productRequestDTO.getName());
        product.setWidth(productRequestDTO.getWidth());
        product.setHeight(productRequestDTO.getHeight());
        product.setLength(productRequestDTO.getLength());
        product.setUsed(productRequestDTO.isUsed());
        product.setProductVolume(productRequestDTO.getProductVolume());
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product readProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}