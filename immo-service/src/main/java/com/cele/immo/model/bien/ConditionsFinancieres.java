package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConditionsFinancieres {
    private String conditionsFinancieres;
    private Long honorairesAgence;
    private String paiementHonoraires;
    private Boolean tvaLoyer;
}
