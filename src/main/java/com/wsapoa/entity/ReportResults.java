package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ReportResults {
    @Id
    @GeneratedValue
    private long reportResultId;
    private long reportProductId;
    private long reportPalletId;
    private long areaEfficiency;
    private long productPerLayer;
    private long palletPerContainer;
    private long numberOfLayer;
    private long totalProducts;
}
