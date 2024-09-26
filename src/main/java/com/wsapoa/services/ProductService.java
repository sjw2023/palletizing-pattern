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
    private final ProductRepository productRepository;
    @Override
    public void create(ProductRequestDTO productRequestDTO) {
        Product product = new Product(productRequestDTO);
        productRepository.save(product);
    }

    @Override
    public void update(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productRequestDTO.getName());
        product.setWidth(productRequestDTO.getWidth());
        product.setHeight(productRequestDTO.getHeight());
        product.setLength(productRequestDTO.getLength());
        product.setUsed(productRequestDTO.isUsed());
        product.setProductVolume(productRequestDTO.getProductVolume());
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product read(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}