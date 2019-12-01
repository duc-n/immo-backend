package com.cele.immo.dto;

import com.cele.immo.model.bien.EtatBien;
import lombok.Data;

@Data
public class BienDTO {
    private String id;
    private EtatBien etat;
    private String adresse;
    private String codePostal;

}
