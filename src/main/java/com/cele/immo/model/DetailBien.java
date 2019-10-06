package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailBien {
    private AdresseBien adresseBien;
    private String typeBien;
    private String activite;
    private Activites activites;
    private String nomMagasin;
    private String enseigneProximite;
}
