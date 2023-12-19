package ru.clevertec.knyazev.pdf.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.util.YAMLParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ServiceCheckPDFManagerImplTest {

    private static final String TEST_PROPERTIES = "testApplication.yaml";

    private String createdPDFDocumentPath;

    private ServiceCheckPDFManagerImpl serviceCheckPDFManagerImpl;

    @BeforeEach
    public void setUp() {
        YAMLParser yamlParser = new YAMLParser(TEST_PROPERTIES);

        String tmpl = yamlParser.getProperty("pdf", "templatePath");
        String pdfPath = yamlParser.getProperty("pdf", "resultPath");
        String fontPath = yamlParser.getProperty("pdf", "documentFontPath");
        String fontEncoding = yamlParser.getProperty("pdf", "documentFontEncoding");

        serviceCheckPDFManagerImpl = new ServiceCheckPDFManagerImpl(tmpl, pdfPath, fontPath, fontEncoding);
    }

    @AfterEach
    public void shutDown() {

        try {
            Files.deleteIfExists(Path.of(createdPDFDocumentPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void checkManageShouldCreateServiceCheckPDFDocument() {

        List<ServiceDTO> inputDataObject = List.of(
                ServiceDTO.builder()
                        .id(UUID.randomUUID())
                        .personId(UUID.randomUUID())
                        .localDateTime(LocalDateTime.now())
                        .personName("Vano")
                        .personSurname("Ivano")
                        .serviceName("Справка о доходах")
                        .description("Доходы mou, doxodi")
                        .price(new BigDecimal("37.00"))
                        .build(),
                ServiceDTO.builder()
                        .id(UUID.randomUUID())
                        .personId(UUID.randomUUID())
                        .localDateTime(LocalDateTime.now())
                        .personName("Vano")
                        .personSurname("Ivano")
                        .serviceName("Справка o пароходах")
                        .description("paro xodi mou, paroxodi")
                        .price(new BigDecimal("18.50"))
                        .build()
        );

        createdPDFDocumentPath = serviceCheckPDFManagerImpl.manage(inputDataObject);

        assertAll(
                () -> assertThat(createdPDFDocumentPath).isNotNull()
                        .isNotEmpty(),
                () -> assertThat(Files.exists(Path.of(createdPDFDocumentPath))).isTrue()
        );
    }
}
