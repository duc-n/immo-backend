package com.cele.immo.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static final String logo_path = "/jasper/images/stackextend-logo.png";
    //private final String invoice_template = "/jasper/invoice_template.jrxml";

    private final String invoice_template = "/jasper/jr1.jrxml";

    @Override
    public void generatePdf(Locale locale) throws IOException {
        File pdfFile = File.createTempFile("my-invoice", ".pdf");

        log.info(String.format("Invoice pdf path : %s", pdfFile.getAbsolutePath()));

        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {
            // Load invoice jrxml template.
            final JasperReport jasperReport = loadTemplate();

            // Create parameters map.
            final Map<String, Object> parameters = parameters("order", locale);

            // Create an empty datasource.
            final JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                    jrBeanCollectionDataSource);
            // Export the report to a PDF file
            JasperExportManager.exportReportToPdfStream(jasperPrint, pos);

        } catch (final Exception e) {
            log.error(String.format("An error occured during PDF creation: %s", e));
        }
    }


    /**
     * Fill template order parametres
     *
     * @param order
     * @param locale
     * @return
     */
    private Map<String, Object> parameters(String order, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("logo", getClass().getResourceAsStream(logo_path));
        parameters.put("order", order);
        parameters.put("REPORT_LOCALE", locale);

        return parameters;
    }

    /**
     * Load invoice jrxml template
     *
     * @return
     * @throws JRException
     */
    private JasperReport loadTemplate() throws JRException {

        log.info(String.format("Invoice template path : %s", invoice_template));

        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);

        return JasperCompileManager.compileReport(jasperDesign);
    }


}
