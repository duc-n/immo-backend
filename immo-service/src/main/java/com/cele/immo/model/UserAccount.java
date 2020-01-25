package com.cele.immo.model;

import com.cele.immo.dto.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserAccount implements UserDetails {
    private String id;
    @Email
    @Indexed(unique = true)
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String telephone;

    @Builder.Default()
    private boolean active = true;

    @Builder.Default()
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    public boolean hasAdminRole() {
        return roles.stream().map(role -> role.name()).collect((Collectors.toList())).contains(Role.ROLE_ADMIN.name());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public String getName() {
        return prenom + " " + nom;
    }

    public UserAccount update(UserProfile userProfile) {
        this.setNom(userProfile.getNom());
        this.setPrenom(userProfile.getPrenom());
        this.setTelephone(userProfile.getTelephone());
        this.setUsername(userProfile.getEmail());
        if (StringUtils.hasLength(userProfile.getNewPassword())) {
            this.setPassword(userProfile.getNewPassword());
        }
        return this;
    }
}
