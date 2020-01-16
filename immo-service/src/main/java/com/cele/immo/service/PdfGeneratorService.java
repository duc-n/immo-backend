package com.cele.immo.service;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.Locale;

public interface PdfGeneratorService {
    void generatePdf(Locale locale) throws IOException;

    ByteArrayResource generatePdfResource(Locale locale);
}
