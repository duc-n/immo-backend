package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Visite {
    private String cle;
    private String commentaire;
    private String digicode1;
    private String digicode2;
    private String interphone;
    private String etage;
}
