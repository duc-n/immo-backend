package com.cele.immo.dto;

import lombok.Data;

@Data
public class BienCritere {
    private String typeBien;
    private String consultant;
    private Boolean cessionBail;
    private Boolean locationPure;
    private Boolean locationDroitEntree;
    private Boolean mursLibres;
    private Boolean mursOccupes;
    private String adresse;
    private String departement;
    private String codePostal;
    private Integer loyerMin;
    private Integer loyerMax;
    private Integer prixMin;
    private Integer prixMax;
    private Integer valeurLocativeMin;
    private Integer valeurLocativeMax;
    private Integer surfaceTotaleMin;
    private Integer surfaceTotaleMax;
    private Integer surfaceRDCMin;
    private Integer surfaceRDCMax;
    private Integer lineaireVitrineMin;
    private Boolean angle;

    private Boolean pointRouge;
    private Boolean pointNoir;
    private Boolean avecPanneau;
    private Boolean sansPasserelles;
    private Boolean licenceIV;
    private Boolean liquidationJudiciaire;
    private Boolean popupStore;
    private Boolean restaurentConduitCheminee;
    private Boolean restaurentSansNuisance;
    private Boolean terrasse;
    private Boolean brouillon;
    private Boolean conclu;
    private Boolean actif;
    private Boolean offreEnCours;
    private Boolean archive;

    private String activite;

    // Emplacement
    private Boolean aDefinir;
    private Boolean numero1;
    private Boolean numero1Alimentaire;
    private Boolean numero1PretAPorter;
    private Boolean rueCommercante;
    private Boolean fluxPieton;
    private Boolean axeVoiture;
    private Boolean ruePietonne;
    private Boolean zoneTouristique;
    private Boolean zoneBureau;
    private Boolean zoneResidentielle;
    private Boolean centreCommercial;
    private Boolean zac;
}
