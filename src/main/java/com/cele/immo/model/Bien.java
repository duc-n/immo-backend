package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Bien {
    @Id
    private String id;
    private String nomTitulaire;
    private DetailBien detailBien;
    private Mandat mandat;
    private Bail bail;
    private ConditionsFinancieres conditionsFinancieres;
    private Visite visite;
    private Surface surface;
    private Descriptif descriptif;
    private Photos photos;
    private Videos videos;
    private Communication communication;
}
