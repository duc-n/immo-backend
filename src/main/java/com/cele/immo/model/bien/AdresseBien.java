package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdresseBien {
    private String adresse;
    private String ville;
    private String codePostal;
    private String quatier;
    private String metro;
    private String rer;
    private String bus;
    private String parking;
    private Emplacements emplacements;
    private String origine;
}
