package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Video {
    private String url;
    private String key;
}
