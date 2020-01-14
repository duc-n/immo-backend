package com.cele.immo.service;

import java.io.IOException;
import java.util.Locale;

public interface PdfGeneratorService {
    void generatePdf(Locale locale) throws IOException;
}
