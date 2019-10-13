package com.cele.immo.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;

@Data
@Builder
public class Photo {
    private String title;
    private Binary image;
}
