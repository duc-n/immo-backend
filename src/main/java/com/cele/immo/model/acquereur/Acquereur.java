package com.cele.immo.model.acquereur;


import com.cele.immo.model.Document;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Acquereur {
    private String civilite;
    private String nom;
    private String prenom;
    private Boolean societeEnCreation;
    private String societe;
    private String tel;
    private String email;
    private String emailSupp;
    private String activite;
    private String origine;
    private Boolean topAcquereur;
    private Boolean demandeRdv;
    private Boolean newsLettre;
    private Boolean consentementRGPD;
    private String consultantTitulaire;
    private String consultatSociete;
    private BigDecimal pourcentHonoraires;
    private DetailAcquereur detailAcquereur;
    private List<Contact> contacts;
    private List<String> notes;
    private List<Document> documents;
    @CreatedDate
    public LocalDateTime createdDate;

}
