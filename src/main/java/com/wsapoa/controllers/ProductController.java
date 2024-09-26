//package com.wsapoa.controllers;
//
//import com.wsapoa.dto.ProductRequestDTO;
//import com.wsapoa.entity.Product;
//import com.wsapoa.services.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ProductService productService;
//
//    @PostMapping
//    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
//        productService.createProduct(productRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
//        productService.updateProduct(id, productRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> readProduct(@PathVariable Long id) {
//        Product product = productService.readProduct(id);
//        return ResponseEntity.ok(product);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        return ResponseEntity.ok(products);
//    }
//}
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