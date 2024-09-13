package com.wsapoa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "report_result_pallets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResultPallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportResultPalletId;
    private int orderIndex;
    //TODO : Change to Coordinate object later
    private long x;
    private long y;
    private long z;
    @ManyToOne
    @JoinColumn(name = "report_result_id")
    @JsonIgnore
    private ReportResult reportResult;
}
