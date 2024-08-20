package com.wsapoa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity(name = "report_results")
@Setter
@Getter
public class ReportResult {
    @Id
    @GeneratedValue
    private long reportResultId;
    private long areaEfficiency;
    private long productPerLayer;
    private long palletPerContainer;
    private long numberOfLayers;
    private long totalProducts;
//    @OneToMany(mappedBy = "reportResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "reportResult", fetch=FetchType.EAGER, cascade = CascadeType.ALL) // (1)
    List<ReportResultProduct> reportResultProducts;
    @OneToMany(mappedBy = "reportResult", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReportResultPallet> reportResultPallets;
}
