package com.cele.immo.model;

import lombok.Data;

@Data
public class Page {
    private int size;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
}
