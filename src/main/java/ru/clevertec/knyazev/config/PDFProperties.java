package ru.clevertec.knyazev.config;

import lombok.Builder;

@Builder
public record PDFProperties (
    String pdfTemplatePath,
    String pdfPath,
    String pdfFontPath,
    String pdfFontEncoding
) {}
