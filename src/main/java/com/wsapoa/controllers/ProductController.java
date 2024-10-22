package com.wsapoa.controllers;

import com.wsapoa.dto.ProductRequestDTO;
import com.wsapoa.entity.Product;
import com.wsapoa.services.BaseService;
import com.wsapoa.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController extends BaseController<Product, Long, ProductRequestDTO> {

    private final ProductService productService;

    @Override
    protected BaseService<Product, Long, ProductRequestDTO> getService() {
        return productService;
    }
}