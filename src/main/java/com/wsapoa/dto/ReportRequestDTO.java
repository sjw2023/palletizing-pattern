package com.wsapoa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO{
    private long palletId;
    private long productId;
    private long containerId;
    private long patternId;
    private long marginSetting;
    private long exceedLengthSetting;
    private String patternType;
    private String ContainerType;
}
