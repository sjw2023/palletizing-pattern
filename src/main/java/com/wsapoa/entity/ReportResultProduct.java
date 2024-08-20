package com.wsapoa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "report_result_products")
@Setter
@Getter
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
    @JoinColumn
    private ReportResult reportResult;
}
