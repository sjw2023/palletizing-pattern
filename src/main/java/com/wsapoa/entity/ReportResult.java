// src/main/java/com/wsapoa/entity/ReportResult.java
package com.wsapoa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "report_results")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResult {
    @Id
    @GeneratedValue
    private long reportResultId;
    private float patternAreaEfficiency;
    private long productPerLayer;
    private long numberOfPatternsInContainer;
    private float containerAreaEfficiency;
    private long numberOfLayers;
    private long totalProducts;
    private long usedProduct;
    private long usedPallet;
    private long usedContainer;
    protected String patternType;
    protected long totalProductsInContainer;

//    @JsonIgnore
    @OneToMany(mappedBy = "reportResult", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReportResultProduct> reportResultProducts = new ArrayList<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "reportResult", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReportResultPallet> reportResultPallets = new ArrayList<>();

    @JsonIgnore
    public void addReportResultProduct(@NotNull ReportResultProduct reportResultProduct) {
        if (reportResultProducts == null) {
            reportResultProducts = new ArrayList<>();
        }
        reportResultProducts.add(reportResultProduct);
        reportResultProduct.setReportResult(this);
    }

//    public void removeReportResultProduct(@NotNull ReportResultProduct reportResultProduct) {
//        if (reportResultProducts != null) {
//            reportResultProducts.remove(reportResultProduct);
//            reportResultProduct.setReportResult(null);
//        }
//    }

    public void addReportResultPallet(@NotNull ReportResultPallet reportResultPallet) {
        if (reportResultPallets == null) {
            reportResultPallets = new ArrayList<>();
        }
        reportResultPallets.add(reportResultPallet);
        reportResultPallet.setReportResult(this);
    }

//    public void removeReportResultPallet(@NotNull ReportResultPallet reportResultPallet) {
//        if (reportResultPallets != null) {
//            reportResultPallets.remove(reportResultPallet);
//            reportResultPallet.setReportResult(null);
//        }
//    }
}