package com.wsapoa.entity;

import com.wsapoa.dto.ProductRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private long productId;
    private String name;
    private long width;
    private long height;
    private long length;
    private boolean used;
    private long productVolume;

    public Product(ProductRequestDTO productRequestDTO) {
        this.name = productRequestDTO.getName();
        this.width = productRequestDTO.getWidth();
        this.height = productRequestDTO.getHeight();
        this.length = productRequestDTO.getLength();
        this.used = productRequestDTO.isUsed();
        this.productVolume = productRequestDTO.getProductVolume();
    }
}