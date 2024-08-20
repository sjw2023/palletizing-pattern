package com.wsapoa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "report_result_pallets")
@Getter
@Setter
public class ReportResultPallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportResultPalletId;
    private int orderIndex;
    //TODO : Change to Coordinate object later
    private int x;
    private int y;
    private int z;
    @ManyToOne
    @JoinColumn(name = "report_result_id")
    private ReportResult reportResult;
}
