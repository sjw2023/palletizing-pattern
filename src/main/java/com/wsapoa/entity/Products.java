package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Products {
    @Id
    @GeneratedValue
    private long productId;
    private String name;
    private long width;
    private long height;
    private long length;
    private boolean used;
    private long productVolume;
}
