package com.cele.immo.controller;

import com.cele.immo.service.PdfGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController()
@RequestMapping(value = "/pdf")
@Slf4j
public class PdfGeneratorController {
    @Autowired
    PdfGeneratorService pdfGeneratorService;

    @GetMapping(path = "/contract", produces = "application/pdf")
    ResponseEntity<Resource> getPDF() {
        Resource pdfFile = pdfGeneratorService.generatePdfResource(Locale.FRANCE);

        //log.debug("pdfFile.contentLength() {}", pdfFile.contentLength());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("pdf", "pdf");
        return ResponseEntity
                .ok().cacheControl(CacheControl.noCache())
                .headers(headers).body(pdfFile);
    }
}
