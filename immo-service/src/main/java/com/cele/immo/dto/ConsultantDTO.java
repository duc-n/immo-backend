package com.cele.immo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultantDTO {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;
    private String username;
}
