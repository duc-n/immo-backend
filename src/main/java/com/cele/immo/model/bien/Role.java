package com.cele.immo.model.bien;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Data
@Document
public class Role implements GrantedAuthority {
    @Id
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}