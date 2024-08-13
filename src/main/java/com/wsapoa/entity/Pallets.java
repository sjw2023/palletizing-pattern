package com.wsapoa.entity;

import com.wsapoa.dto.PalletRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pallets {
    @Id
    @GeneratedValue
    private long palletId;
    private String name;
    private long width;
    private long height;
    private long length;
    private boolean used;

    public Pallets(PalletRequestDTO palletRequestDTO) {
        this.name = palletRequestDTO.getName();
        this.width = palletRequestDTO.getWidth();
        this.height = palletRequestDTO.getHeight();
        this.length = palletRequestDTO.getLength();
        this.used = palletRequestDTO.isUsed();
    }
}
