package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reports {
    @Id
    @GeneratedValue
    private long reportId;
    private long reportResultId;
    private long productId;
    private long palletId;
    private long containerId;
    private long patternId;
    private String globalConfigurationSetting;
}
