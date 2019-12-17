package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Communication {
    private Boolean siteWeb;
    private Boolean parselle;
    private String bureauLocal;
    private String groupeSeloger;
    private String lebonCoin;
    private String territoireMaking;
    private String transactionCommerce;
    private Boolean alertesEmailRapprochement;
    private Boolean informationsMandats;
}
