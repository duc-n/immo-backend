package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bail {
    private String typeBail;
    private Boolean bailNotarie;
    private Long chargeMensuel;
    private String periodLoyer;
    private String depotGarantie;
    private String cautionSup;
    private Long taxFonctiere;
    private String paiementTaxFonctiere;
    private String paiement606;
}
