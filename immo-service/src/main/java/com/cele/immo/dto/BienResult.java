package com.cele.immo.dto;

import com.cele.immo.model.UserAccount;
import com.cele.immo.model.bien.EtatBien;
import lombok.Data;

@Data
public class BienResult {
    private String id;
    private EtatBien etat;
    private String adresse;
    private String codePostal;
    private UserAccount consultant;

}
