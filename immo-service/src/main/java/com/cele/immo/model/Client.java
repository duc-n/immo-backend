package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document
public class Client {
    @Id
    private String id;
    private String typeClient;
    private String typeContact;
    private Civilite civilite;
    @TextIndexed
    private String nom;
    @TextIndexed
    private String prenom;
    private Boolean societeEnCreation;
    private String societe;
    private String adresse;
    private String tel;
    private String email;
    private String emailSupp;
    private String activite;
    private Boolean topAcquereur;
    private Boolean demandeRdv;
    private Boolean newsLettre;
    private Boolean consentementRGPD;
    private Boolean alertEmail;

    //@CascadeSave
    //@DBRef
    private List<String> consultantIds;
}
