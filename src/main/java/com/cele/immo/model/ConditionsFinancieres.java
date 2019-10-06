package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConditionsFinancieres {
    private String conditionsFinancieres;
    private Long honorairesAgence;
    private Long paiementHonoraires;
    private BigDecimal tvaLoyer;
}
