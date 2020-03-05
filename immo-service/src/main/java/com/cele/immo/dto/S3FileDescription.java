package com.cele.immo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3FileDescription {
    private String url;
    private String key;
    private String bienId;
    private String username;
}
