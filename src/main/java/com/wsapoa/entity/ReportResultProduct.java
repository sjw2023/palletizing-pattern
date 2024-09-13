package com.wsapoa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "report_result_products")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResultProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportResultProductId;
    private int orderIndex;
    boolean rotate;
    //TODO : Change to Coordinate object later
    private long x;
    private long y;
    private long z;
    @ManyToOne
    @JoinColumn(name = "report_result_id")
    @JsonIgnore
    private ReportResult reportResult;

    public ReportResultProduct(ObjectAreaInfo objectAreaInfo, int orderIndex) {
        this.x = objectAreaInfo.getCenter().getX();
        this.y = objectAreaInfo.getCenter().getY();
        this.z = objectAreaInfo.getCenter().getZ();
        this.rotate = objectAreaInfo.isRotated();
        this.orderIndex = orderIndex;
    }
}
