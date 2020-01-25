package com.cele.immo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private String nom;
    private String prenom;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String reNewPassword;
    private String telephone;
}
