package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patterns {
    @Id
    @GeneratedValue
    private long patternId;
    private String name;
    private long width;
    private long height;
    private long length;
    private boolean used;
    private long patternVolume;
}
