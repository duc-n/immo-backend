package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Emplacements {
    private Boolean aDefinir;
    private Boolean numero1;
    private Boolean numero1Alimentaire;
    private Boolean numero1PretPorter;
    private Boolean rueCommercante;
    private Boolean fluxPietons;
    private Boolean axeVoiture;
    private Boolean ruePietonne;
    private Boolean zoneTouristique;
    private Boolean zoneBureau;
    private Boolean zoneResidentielle;
    private Boolean centreCommercial;
    private Boolean zac;
}
