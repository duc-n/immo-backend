package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Descriptif {
    private String localIdealPour;
    private BigDecimal longueurFacade;
    private Boolean facadeAngle;
    private String description;
    private Boolean espaceExterieur;
    private Boolean hsp2m80;
    private Boolean pointEau;
    private Boolean wc;
    private Boolean disponible;
    private String noteConfidentielle;
}
