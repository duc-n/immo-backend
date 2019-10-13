package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Document {
    private String id;
    private String title;
}
