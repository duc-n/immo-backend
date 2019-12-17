package com.cele.immo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConsultantDTO {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;
    private String username;
}
