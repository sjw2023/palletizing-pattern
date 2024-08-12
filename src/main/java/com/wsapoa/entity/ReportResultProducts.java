package com.wsapoa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ReportResultProducts {
    @Id
    @GeneratedValue
    private long reportResultId;
    private int orderIndex;
    //TODO : Change to Coordinate object later
    private int x;
    private int y;
    private int z;
}
