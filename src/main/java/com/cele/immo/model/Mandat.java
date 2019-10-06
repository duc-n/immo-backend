package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mandat {
    private String transaction;
    private Boolean panneau;
    private Long prixVente;
    private String typeMandat;
}
