package com.cele.immo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegister {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String repassword;
    private String telephone;
    private Boolean admin;
}
