package com.wsapoa.entity;

import com.wsapoa.dto.ReportRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reports")
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue
    private long reportId;
    private long productId;
    private long palletId;
    private long containerId;
    private long patternId;
    private long marginSetting;
    private long exceedLengthSetting;

    public Report(ReportRequestDTO reportRequestDTO) {
        this.containerId = reportRequestDTO.getContainerId();
        this.palletId = reportRequestDTO.getPalletId();
        this.patternId = reportRequestDTO.getPatternId();
        this.productId = reportRequestDTO.getProductId();
        this.exceedLengthSetting = reportRequestDTO.getExceedLengthSetting();
        this.marginSetting = reportRequestDTO.getMarginSetting();
    }
}
