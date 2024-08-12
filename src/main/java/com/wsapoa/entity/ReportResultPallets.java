package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReportResultPallets {
    @Id
    @GeneratedValue
    private long ReportResultPalletId;
    private int orderIndex;
    //TODO : Change to Coordinate object later
    private int x;
    private int y;
    private int z;
}
