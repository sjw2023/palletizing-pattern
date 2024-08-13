package com.wsapoa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull
    private String name;

    @Min(1)
    private long width;

    @Min(1)
    private long height;

    @Min(1)
    private long length;

    @NotNull
    private boolean used;

    @Min(1)
    private long productVolume;
}