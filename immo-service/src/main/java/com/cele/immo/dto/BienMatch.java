package com.cele.immo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BienMatch {
    private String criteriaName;
    private Object valueMin;
    private Object valueMax;
}
