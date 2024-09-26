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
    private long productId;
    private long marginSetting;
    private long exceedLengthSetting;
    private String ContainerType;
}
