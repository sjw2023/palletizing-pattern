package com.wsapoa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PalletRequestDTO {

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
}