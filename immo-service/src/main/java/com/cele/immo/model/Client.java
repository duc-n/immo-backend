package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document
public class Client {
    private String typeClient;
    private String typeContact;
    private String civilite;
    private String nom;
    private String prenom;
    private Boolean societeEnCreation;
    private String societe;
    private String adresse;
    private String tel;
    private String email;
    private String emailSupp;
    private String activite;
    private Boolean demandeRdv;
    private Boolean newsLettre;
    private Boolean consentementRGPD;
    private Boolean alertEmail;

    @DBRef
    //@CascadeSave
    private List<UserAccount> consultants;
}
