package com.wsapoa.entity;

import com.wsapoa.dto.ContainerRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Containers {
    @Id
    @GeneratedValue
    private long containerId;
    private String name;
    private long width;
    private long height;
    private long length;
    private boolean used;
    private long containerVolume;

    public Containers(ContainerRequestDTO containerRequestDTO) {
        this.name = containerRequestDTO.getName();
        this.width = containerRequestDTO.getWidth();
        this.height = containerRequestDTO.getHeight();
        this.length = containerRequestDTO.getLength();
        this.used = containerRequestDTO.isUsed();
        this.containerVolume = containerRequestDTO.getContainerVolume();
    }
}