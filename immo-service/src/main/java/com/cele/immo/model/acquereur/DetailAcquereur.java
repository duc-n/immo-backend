package com.cele.immo.model.acquereur;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailAcquereur {

    private String type;
    private String tel;
    private String activite;
    private String nomSociete;
    private String adresse;
}
